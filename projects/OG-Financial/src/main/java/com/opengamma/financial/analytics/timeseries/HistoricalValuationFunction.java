/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.timeseries;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.target.ComputationTargetReference;
import com.opengamma.engine.target.ComputationTargetReferenceVisitor;
import com.opengamma.engine.target.ComputationTargetRequirement;
import com.opengamma.engine.target.ComputationTargetResolverUtils;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.engine.view.ViewCalculationConfiguration;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.temptarget.TempTarget;
import com.opengamma.financial.temptarget.TempTargetRepository;
import com.opengamma.financial.view.ViewEvaluationResult;
import com.opengamma.financial.view.ViewEvaluationTarget;
import com.opengamma.id.ExternalBundleIdentifiable;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ExternalIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.util.timeseries.TimeSeries;

/**
 * Iterates a view client over historical data to produce a historical valuation of a target. The view client iteration is performed by a helper function on a {@link ViewEvaluationTarget} created by
 * this function. The time series appropriate to this function's target are then extracted from the overall evaluation results.
 */
public class HistoricalValuationFunction extends AbstractFunction.NonCompiledInvoker {

  private static final Logger s_logger = LoggerFactory.getLogger(HistoricalValuationFunction.class);

  /**
   * Property naming the value produced on the target to generate the time series. For example {@code Historical Series[Value=FairValue]} will produce a time series based on evaluating
   * {@code FairValue[]}.
   */
  public static final String VALUE = "Value";

  /**
   * Prefix on properties corresponding the the underlying production on the target. For example {@code Historical Series[Value=FairValue, Value_Foo=Bar]} will produce a time series based on
   * evaluating {@code FairValue[Foo=Bar]}.
   */
  public static final String PASSTHROUGH_PREFIX = VALUE + "_";

  /**
   * Property naming how the target is specified, for example by unique identifier, object identifier or external identifier.
   */
  public static final String TARGET_SPECIFICATION = "Target";

  /**
   * Value of the {@link #TARGET_SPECIFICATION} property indicating the target is specified by its unique identifier and is independent of the resolver version/correction time on the spawned view
   * cycles.
   */
  public static final String TARGET_SPECIFICATION_UNIQUE = "Unique";

  /**
   * Value of the {@link #TARGET_SPECIFICATION} property indicating the target is specified by its object identifier and is dependent of the resolver version/correction time on the spawned view
   * cycles.
   */
  public static final String TARGET_SPECIFICATION_OBJECT = "Object";

  /**
   * Value of the {@link #TARGET_SPECIFICATION} property indicating the target is specified by its external identifier bundle and is dependent of the resolver version/correction time on the spawned
   * view cycles.
   */
  public static final String TARGET_SPECIFICATION_EXTERNAL = "External";

  protected ValueRequirement getNestedRequirement(final ComputationTargetResolver.AtVersionCorrection resolver, final ComputationTarget target, final ValueProperties constraints,
      final ViewEvaluationTarget updateTempTarget) {
    String valueName = ValueRequirementNames.VALUE;
    ComputationTargetReference requirementTarget = null;
    final ValueProperties.Builder requirementConstraints = ValueProperties.builder();
    if (constraints.getProperties() != null) {
      for (final String constraintName : constraints.getProperties()) {
        final Set<String> constraintValues = constraints.getValues(constraintName);
        if (VALUE.equals(constraintName)) {
          if (constraintValues.isEmpty()) {
            valueName = ValueRequirementNames.VALUE;
          } else if (constraintValues.size() > 1) {
            return null;
          } else {
            valueName = constraintValues.iterator().next();
          }
        } else if (TARGET_SPECIFICATION.equals(constraintName)) {
          if (constraintValues.isEmpty() || constraintValues.contains(TARGET_SPECIFICATION_UNIQUE)) {
            requirementTarget = target.toSpecification();
          } else if (constraintValues.contains(TARGET_SPECIFICATION_OBJECT)) {
            final ComputationTargetSpecification targetSpec = target.toSpecification();
            if (targetSpec.getUniqueId() != null) {
              requirementTarget = ComputationTargetResolverUtils.simplifyType(targetSpec.replaceIdentifier(target.getUniqueId().toLatest()), resolver);
            } else {
              // Null - special case
              requirementTarget = targetSpec;
            }
          } else if (constraintValues.contains(TARGET_SPECIFICATION_EXTERNAL)) {
            final ExternalIdBundle identifiers;
            if (target.getValue() instanceof ExternalIdentifiable) {
              final ExternalId identifier = ((ExternalIdentifiable) target.getValue()).getExternalId();
              if (identifier == null) {
                identifiers = ExternalIdBundle.EMPTY;
              } else {
                identifiers = identifier.toBundle();
              }
            } else if (target.getValue() instanceof ExternalBundleIdentifiable) {
              identifiers = ((ExternalBundleIdentifiable) target.getValue()).getExternalIdBundle();
            } else if (target.getValue() == null) {
              // Null - special case
              identifiers = ExternalIdBundle.EMPTY;
            } else {
              return null;
            }
            if (target.getContextIdentifiers() == null) {
              requirementTarget = new ComputationTargetRequirement(resolver.simplifyType(target.getType()), identifiers);
            } else {
              requirementTarget = target.getContextSpecification().containing(resolver.simplifyType(target.getLeafSpecification().getType()), identifiers);
            }
          } else {
            return null;
          }
        } else if (updateTempTarget != null) {
          if (constraintName.startsWith(PASSTHROUGH_PREFIX)) {
            final String name = constraintName.substring(PASSTHROUGH_PREFIX.length());
            if (constraintValues.isEmpty()) {
              requirementConstraints.withAny(name);
            } else {
              requirementConstraints.with(name, constraintValues);
            }
            if (constraints.isOptional(constraintName)) {
              requirementConstraints.withOptional(name);
            }
          } else if (HistoricalTimeSeriesFunctionUtils.START_DATE_PROPERTY.equals(constraintName)) {
            if (constraintValues.isEmpty()) {
              updateTempTarget.setFirstValuationDate("");
            } else {
              updateTempTarget.setFirstValuationDate(constraintValues.iterator().next());
            }
          } else if (HistoricalTimeSeriesFunctionUtils.INCLUDE_START_PROPERTY.equals(constraintName)) {
            if (constraintValues.isEmpty() || constraintValues.contains(HistoricalTimeSeriesFunctionUtils.YES_VALUE)) {
              updateTempTarget.setIncludeFirstValuationDate(true);
            } else if (constraintValues.contains(HistoricalTimeSeriesFunctionUtils.NO_VALUE)) {
              updateTempTarget.setIncludeFirstValuationDate(false);
            } else {
              return null;
            }
          } else if (HistoricalTimeSeriesFunctionUtils.END_DATE_PROPERTY.equals(constraintName)) {
            if (constraintValues.isEmpty()) {
              updateTempTarget.setLastValuationDate("");
            } else {
              updateTempTarget.setLastValuationDate(constraintValues.iterator().next());
            }
          } else if (HistoricalTimeSeriesFunctionUtils.INCLUDE_END_PROPERTY.equals(constraintName)) {
            if (constraintValues.isEmpty() || constraintValues.contains(HistoricalTimeSeriesFunctionUtils.YES_VALUE)) {
              updateTempTarget.setIncludeLastValuationDate(true);
            } else if (constraintValues.contains(HistoricalTimeSeriesFunctionUtils.NO_VALUE)) {
              updateTempTarget.setIncludeLastValuationDate(false);
            } else {
              return null;
            }
          } else if (!ValuePropertyNames.FUNCTION.equals(constraintName) && !constraints.isOptional(constraintName)) {
            return null;
          }
        }
      }
    }
    if (requirementTarget == null) {
      requirementTarget = ComputationTargetResolverUtils.simplifyType(target.toSpecification(), resolver);
    }
    return new ValueRequirement(valueName, requirementTarget, requirementConstraints.get());
  }

  // FunctionDefinition

  @Override
  public void init(final FunctionCompilationContext context) {
    if (OpenGammaCompilationContext.getTempTargets(context) == null) {
      throw new IllegalStateException("Function compilation context does not contain " + OpenGammaCompilationContext.TEMPORARY_TARGETS_NAME);
    }
  }

  // CompiledFunctionDefinition

  @Override
  public ComputationTargetType getTargetType() {
    return ComputationTargetType.ANYTHING;
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
    return Collections.singleton(new ValueSpecification(ValueRequirementNames.HISTORICAL_TIME_SERIES, target.toSpecification(), ValueProperties.all()));
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final ViewEvaluationTarget tempTarget = new ViewEvaluationTarget(context.getViewCalculationConfiguration().getViewDefinition().getMarketDataUser());
    // TODO: get a suitable time zone for the target -- use constraint injection
    // TODO: get a suitable valuation time for the target -- use constraint injection
    tempTarget.setCorrection(context.getComputationTargetResolver().getVersionCorrection().getCorrectedTo());
    final ViewCalculationConfiguration calcConfig = new ViewCalculationConfiguration(tempTarget.getViewDefinition(), context.getViewCalculationConfiguration().getName());
    calcConfig.addSpecificRequirement(getNestedRequirement(context.getComputationTargetResolver(), target, desiredValue.getConstraints(), tempTarget));
    tempTarget.getViewDefinition().addViewCalculationConfiguration(calcConfig);
    final TempTargetRepository targets = OpenGammaCompilationContext.getTempTargets(context);
    final UniqueId requirement = targets.locateOrStore(tempTarget);
    return Collections.singleton(new ValueRequirement(ValueRequirementNames.VALUE, new ComputationTargetSpecification(TempTarget.TYPE, requirement), ValueProperties.none()));
  }

  protected ValueProperties.Builder createValueProperties(final ViewEvaluationTarget tempTarget) {
    final ValueProperties.Builder builder = createValueProperties();
    builder.with(HistoricalTimeSeriesFunctionUtils.START_DATE_PROPERTY, tempTarget.getFirstValuationDate());
    builder.with(HistoricalTimeSeriesFunctionUtils.INCLUDE_START_PROPERTY, tempTarget.isIncludeFirstValuationDate() ? HistoricalTimeSeriesFunctionUtils.YES_VALUE
        : HistoricalTimeSeriesFunctionUtils.NO_VALUE);
    builder.with(HistoricalTimeSeriesFunctionUtils.END_DATE_PROPERTY, tempTarget.getLastValuationDate());
    builder.with(HistoricalTimeSeriesFunctionUtils.INCLUDE_END_PROPERTY, tempTarget.isIncludeLastValuationDate() ? HistoricalTimeSeriesFunctionUtils.YES_VALUE
        : HistoricalTimeSeriesFunctionUtils.NO_VALUE);
    return builder;
  }

  // TODO: Our declared type of anything means there will never be a parent context, this will probably need fixing

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target, final Map<ValueSpecification, ValueRequirement> inputs) {
    final TempTarget tempTargetObject = OpenGammaCompilationContext.getTempTargets(context).get(inputs.keySet().iterator().next().getTargetSpecification().getUniqueId());
    if (tempTargetObject instanceof ViewEvaluationTarget) {
      final ViewEvaluationTarget tempTarget = (ViewEvaluationTarget) tempTargetObject;
      final ViewCalculationConfiguration calcConfig = tempTarget.getViewDefinition().getCalculationConfiguration(context.getViewCalculationConfiguration().getName());
      final ExternalIdBundle targetEids;
      if (target.getValue() instanceof ExternalIdentifiable) {
        targetEids = ((ExternalIdentifiable) target.getValue()).getExternalId().toBundle();
      } else if (target.getValue() instanceof ExternalBundleIdentifiable) {
        targetEids = ((ExternalBundleIdentifiable) target.getValue()).getExternalIdBundle();
      } else {
        targetEids = null;
      }
      final ComputationTargetSpecification targetSpec = target.toSpecification();
      final ComputationTargetSpecification targetContextSpec = target.getContextSpecification();
      final ComputationTargetReferenceVisitor<Set<String>> getTargetType = new ComputationTargetReferenceVisitor<Set<String>>() {

        @Override
        public Set<String> visitComputationTargetRequirement(final ComputationTargetRequirement requirement) {
          if (target.getUniqueId() == null) {
            if (requirement.getIdentifiers().isEmpty()) {
              if (ObjectUtils.equals(requirement.getParent(), targetContextSpec)) {
                // Null target can be referenced by anything
                return ImmutableSet.of(TARGET_SPECIFICATION_OBJECT, TARGET_SPECIFICATION_UNIQUE, TARGET_SPECIFICATION_EXTERNAL);
              }
            }
          } else {
            if ((targetEids != null) && targetEids.equals(requirement.getIdentifiers())) {
              if (ObjectUtils.equals(requirement.getParent(), targetContextSpec)) {
                // Our target
                return Collections.singleton(TARGET_SPECIFICATION_EXTERNAL);
              }
            }
          }
          // Not our target
          return null;
        }

        @Override
        public Set<String> visitComputationTargetSpecification(final ComputationTargetSpecification specification) {
          if (target.getUniqueId() == null) {
            if (specification.getUniqueId() == null) {
              // Null target can be referenced by anything
              return ImmutableSet.of(TARGET_SPECIFICATION_OBJECT, TARGET_SPECIFICATION_UNIQUE, TARGET_SPECIFICATION_EXTERNAL);
            }
          } else if (target.getUniqueId().isLatest()) {
            // The target is a primitive - unique and object are the same
            if (target.getUniqueId().equals(specification.getUniqueId())) {
              if (ObjectUtils.equals(specification.getParent(), targetContextSpec)) {
                // Our target
                return ImmutableSet.of(TARGET_SPECIFICATION_OBJECT, TARGET_SPECIFICATION_UNIQUE);
              }
            }
          } else {
            if (specification.getUniqueId() != null) {
              if (specification.getUniqueId().isLatest()) {
                if (target.getUniqueId().equalObjectId(specification.getUniqueId())) {
                  if (ObjectUtils.equals(specification.getParent(), targetContextSpec)) {
                    // Our target at object specification
                    return Collections.singleton(TARGET_SPECIFICATION_OBJECT);
                  }
                }
              } else {
                if (target.getUniqueId().equals(specification.getUniqueId())) {
                  if (ObjectUtils.equals(specification.getParent(), targetContextSpec)) {
                    // Our target at unique specification
                    return Collections.singleton(TARGET_SPECIFICATION_UNIQUE);
                  }
                }
              }
            }
          }
          // Not our target
          return null;
        }

      };
      final Set<ValueSpecification> results = new HashSet<ValueSpecification>();
      for (final ValueRequirement nestedRequirement : calcConfig.getSpecificRequirements()) {
        final Set<String> targetType = nestedRequirement.getTargetReference().accept(getTargetType);
        if (targetType != null) {
          // The properties on the outputs are based directly on the constraints used to specify the nested view definition. We can't
          // get the strict value specifications because we don't know how those requirements will compile because we don't know the
          // valuation date and the graph building behavior of the functions involved might be valuation date dependent - what if the
          // pricing currency changes over time for example; which do we use for the time series. This isn't always the case, but we'll
          // ignore the forms where we should know the outcomes to avoid complicating matters. A higher priority function should be
          // used to enforce any necessary constraints based on the target's properties.
          final ValueProperties.Builder properties = createValueProperties(tempTarget);
          properties.with(VALUE, nestedRequirement.getValueName());
          final ValueProperties nestedConstraints = nestedRequirement.getConstraints();
          if (nestedConstraints.getProperties() != null) {
            for (final String propertyName : nestedConstraints.getProperties()) {
              final Set<String> propertyValues = nestedConstraints.getValues(propertyName);
              final String passthroughName = PASSTHROUGH_PREFIX + propertyName;
              if (propertyValues == null) {
                properties.withAny(passthroughName);
              } else {
                properties.with(passthroughName, propertyValues);
              }
              if (nestedConstraints.isOptional(propertyName)) {
                properties.withOptional(passthroughName);
              }
            }
          }
          results.add(new ValueSpecification(ValueRequirementNames.HISTORICAL_TIME_SERIES, targetSpec, properties.get()));
        }
      }
      return results;
    } else {
      return null;
    }
  }

  // FunctionInvoker

  @Override
  public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target, final Set<ValueRequirement> desiredValues) {
    final ViewEvaluationResult evaluationResult = (ViewEvaluationResult) inputs.getValue(ValueRequirementNames.VALUE);
    final Set<ComputedValue> results = Sets.newHashSetWithExpectedSize(desiredValues.size());
    final ComputationTargetSpecification targetSpec = target.toSpecification();
    for (final ValueRequirement desiredValue : desiredValues) {
      final ValueRequirement requirement = getNestedRequirement(executionContext.getComputationTargetResolver(), target, desiredValue.getConstraints(), null);
      if (requirement != null) {
        final TimeSeries ts = evaluationResult.getTimeSeries(requirement);
        if (ts != null) {
          results.add(new ComputedValue(new ValueSpecification(desiredValue.getValueName(), targetSpec, desiredValue.getConstraints()), ts));
        } else {
          s_logger.warn("Nested requirement {} did not produce a time series for {}", requirement, desiredValue);
        }
      } else {
        s_logger.error("Couldn't produce nested requirement for {}", desiredValue);
      }
    }
    return results;
  }

}