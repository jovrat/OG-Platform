/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.opengamma.core.position.Position;
import com.opengamma.core.position.Trade;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.analytics.fixedincome.YieldCurveNodeSensitivityDataBundle;
import com.opengamma.util.money.Currency;
import com.opengamma.util.money.MultipleCurrencyAmount;
import com.opengamma.util.tuple.DoublesPair;

/**
 * Able to scale values produced by the rest of the OG-Financial package.
 */
public class PositionTradeScalingFunction extends AbstractFunction.NonCompiledInvoker {

  private final String _requirementName;

  public PositionTradeScalingFunction(final String requirementName) {
    Validate.notNull(requirementName, "Requirement name");
    _requirementName = requirementName;
  }

  @Override
  public String getShortName() {
    return "PositionTradeScaling for " + _requirementName;
  }

  @Override
  public ComputationTargetType getTargetType() {
    return ComputationTargetType.POSITION;
  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    return !target.getPosition().getTrades().isEmpty();
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
    final ValueSpecification specification = new ValueSpecification(_requirementName, target.toSpecification(), ValueProperties.all());
    return Collections.singleton(specification);
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final Position position = target.getPosition();
    final Collection<Trade> trades = position.getTrades();
    final Set<ValueRequirement> result = new HashSet<ValueRequirement>();
    final ValueProperties inputConstraint = desiredValue.getConstraints().withoutAny(ValuePropertyNames.FUNCTION);
    for (final Trade trade : trades) {
      result.add(new ValueRequirement(_requirementName, ComputationTargetType.TRADE, trade.getUniqueId(), inputConstraint));
    }
    return result;
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target, final Map<ValueSpecification, ValueRequirement> inputs) {
    // Result properties are anything that was common to the input specifications
    ValueProperties common = null;
    for (ValueSpecification input : inputs.keySet()) {
      common = SumUtils.addProperties(common, input.getProperties());
    }
    if (common == null) {
      // Can't have been any inputs ... ?
      return null;
    }
    common = common.copy().withoutAny(ValuePropertyNames.FUNCTION).with(ValuePropertyNames.FUNCTION, getUniqueId()).get();
    return Collections.singleton(new ValueSpecification(_requirementName, target.toSpecification(), common));
  }

  @Override
  public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target, final Set<ValueRequirement> desiredValues) {
    Object scaledValue = null;
    ValueProperties common = null;
    // TODO: What if there are multiple trades? The original function requested them all as inputs and chose an arbitrary one here. We process them all as the
    // intersection of properties is required for the result
    for (ComputedValue input : inputs.getAllValues()) {
      final Object value = input.getValue();
      if (value instanceof Double) {
        Double doubleValue = (Double) value;
        final double quantity = target.getPosition().getQuantity().doubleValue();
        doubleValue *= quantity;
        scaledValue = doubleValue;
      } else if (value instanceof YieldCurveNodeSensitivityDataBundle) {
        final YieldCurveNodeSensitivityDataBundle nodeSensitivities = (YieldCurveNodeSensitivityDataBundle) value;
        final double quantity = target.getPosition().getQuantity().doubleValue();
        final Currency ccy = nodeSensitivities.getCurrency();
        final String name = nodeSensitivities.getYieldCurveName();
        final DoubleLabelledMatrix1D m = nodeSensitivities.getLabelledMatrix();
        final double[] scaled = getScaledMatrix(m.getValues(), quantity);
        scaledValue = new YieldCurveNodeSensitivityDataBundle(ccy, new DoubleLabelledMatrix1D(m.getKeys(), m.getLabels(), scaled), name);
      } else if (value instanceof DoubleLabelledMatrix1D) {
        final DoubleLabelledMatrix1D m = (DoubleLabelledMatrix1D) value;
        final double quantity = target.getPosition().getQuantity().doubleValue();
        final double[] scaled = getScaledMatrix(m.getValues(), quantity);
        scaledValue = new DoubleLabelledMatrix1D(m.getKeys(), m.getLabels(), scaled);
      } else if (value instanceof LocalDateLabelledMatrix1D) {
        final LocalDateLabelledMatrix1D m = (LocalDateLabelledMatrix1D) value;
        final double quantity = target.getPosition().getQuantity().doubleValue();
        final double[] scaled = getScaledMatrix(m.getValues(), quantity);
        scaledValue = new LocalDateLabelledMatrix1D(m.getKeys(), m.getLabels(), scaled);
      } else if (value instanceof ZonedDateTimeLabelledMatrix1D) {
        final ZonedDateTimeLabelledMatrix1D m = (ZonedDateTimeLabelledMatrix1D) value;
        final double quantity = target.getPosition().getQuantity().doubleValue();
        final double[] scaled = getScaledMatrix(m.getValues(), quantity);
        scaledValue = new ZonedDateTimeLabelledMatrix1D(m.getKeys(), m.getLabels(), scaled);
      } else if (value instanceof CurrencyLabelledMatrix1D) {
        final CurrencyLabelledMatrix1D m = (CurrencyLabelledMatrix1D) value;
        final double quantity = target.getPosition().getQuantity().doubleValue();
        final double[] scaled = getScaledMatrix(m.getValues(), quantity);
        scaledValue = new CurrencyLabelledMatrix1D(m.getKeys(), m.getLabels(), scaled);
      } else if (value instanceof MultipleCurrencyAmount) {
        final MultipleCurrencyAmount m = (MultipleCurrencyAmount) value;
        final double quantity = target.getPosition().getQuantity().doubleValue();
        scaledValue = m.multipliedBy(quantity);
      } else if (_requirementName.equals(ValueRequirementNames.PRESENT_VALUE_CURVE_SENSITIVITY)) { //TODO this should probably not be done like this
        @SuppressWarnings("unchecked")
        final Map<String, List<DoublesPair>> map = (Map<String, List<DoublesPair>>) value;
        final Map<String, List<DoublesPair>> scaled = new HashMap<String, List<DoublesPair>>();
        for (final Map.Entry<String, List<DoublesPair>> entry : map.entrySet()) {
          final List<DoublesPair> scaledList = new ArrayList<DoublesPair>();
          for (final DoublesPair pair : entry.getValue()) {
            scaledList.add(DoublesPair.of(pair.first, pair.second * target.getPosition().getQuantity().doubleValue()));
          }
          scaled.put(entry.getKey(), scaledList);
        }
        scaledValue = scaled;
      } else if (value instanceof DoubleLabelledMatrix2D) {
        final DoubleLabelledMatrix2D matrix = (DoubleLabelledMatrix2D) value;
        final Double[] xKeys = matrix.getXKeys();
        final Object[] xLabels = matrix.getXLabels();
        final Double[] yKeys = matrix.getYKeys();
        final Object[] yLabels = matrix.getYLabels();
        final double[][] values = matrix.getValues();
        final int n = values.length;
        final int m = values[0].length;
        final double[][] scaledValues = new double[n][m];
        final double scale = target.getPosition().getQuantity().doubleValue();
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < m; j++) {
            scaledValues[i][j] = values[i][j] * scale;
          }
        }
        scaledValue = new DoubleLabelledMatrix2D(xKeys, xLabels, yKeys, yLabels, scaledValues);
      } else if (value instanceof DoubleLabelledMatrix3D) {
        final DoubleLabelledMatrix3D matrix = (DoubleLabelledMatrix3D) value;
        final Double[] xKeys = matrix.getXKeys();
        final Object[] xLabels = matrix.getXLabels();
        final Double[] yKeys = matrix.getYKeys();
        final Object[] yLabels = matrix.getYLabels();
        final Double[] zKeys = matrix.getZKeys();
        final Object[] zLabels = matrix.getZLabels();
        final double[][][] values = matrix.getValues();
        final int n = values.length;
        final int m = values[0].length;
        final int l = values[0][0].length;
        final double[][][] scaledValues = new double[n][m][l];
        final double scale = target.getPosition().getQuantity().doubleValue();
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < m; j++) {
            for (final int k = 0; k < l; j++) {
              scaledValues[i][j][k] = values[i][j][k] * scale;
            }
          }
        }
        scaledValue = new DoubleLabelledMatrix3D(xKeys, xLabels, yKeys, yLabels, zKeys, zLabels, scaledValues);
      } else {
        //REVIEW emcleod 27-1-2011 aaaaaaaaaarrrrrrrrgggggghhhhhhhhh Why is nothing done here?
        scaledValue = value;
      }
      common = SumUtils.addProperties(common, input.getSpecification().getProperties());
    }
    common = common.copy().withoutAny(ValuePropertyNames.FUNCTION).with(ValuePropertyNames.FUNCTION, getUniqueId()).get();
    final ValueSpecification specification = new ValueSpecification(_requirementName, target.toSpecification(), common);
    return Collections.singleton(new ComputedValue(specification, scaledValue));
  }

  private double[] getScaledMatrix(final double[] values, final double quantity) {
    final int n = values.length;
    final double[] scaled = new double[n];
    for (int i = 0; i < n; i++) {
      scaled[i] = values[i] * quantity;
    }
    return scaled;
  }

}
