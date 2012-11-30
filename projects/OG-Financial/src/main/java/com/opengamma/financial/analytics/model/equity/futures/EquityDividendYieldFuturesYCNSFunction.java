/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.futures;

import java.util.NoSuchElementException;
import java.util.Set;

import javax.time.calendar.ZonedDateTime;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.equity.future.pricing.DividendYieldFuturesCalculator;
import com.opengamma.analytics.financial.future.FuturesRatesSensitivityCalculator;
import com.opengamma.analytics.financial.instrument.InstrumentDefinitionWithData;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.YieldCurveBundle;
import com.opengamma.analytics.financial.simpleinstruments.pricing.SimpleFutureDataBundle;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.core.position.Trade;
import com.opengamma.core.value.MarketDataRequirementNames;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.analytics.conversion.FutureSecurityConverter;
import com.opengamma.financial.analytics.ircurve.InterpolatedYieldCurveSpecificationWithSecurities;
import com.opengamma.financial.analytics.model.YieldCurveNodeSensitivitiesHelper;
import com.opengamma.financial.analytics.timeseries.HistoricalTimeSeriesBundle;
import com.opengamma.financial.analytics.timeseries.HistoricalTimeSeriesFunctionUtils;
import com.opengamma.financial.security.FinancialSecurityUtils;
import com.opengamma.financial.security.future.FutureSecurity;
import com.opengamma.util.money.Currency;

/**
 *
 */
public class EquityDividendYieldFuturesYCNSFunction extends EquityDividendYieldFuturesFunction<DoubleMatrix1D> {
  private static final FutureSecurityConverter CONVERTER = new FutureSecurityConverter();

  public EquityDividendYieldFuturesYCNSFunction() {
    super(ValueRequirementNames.YIELD_CURVE_NODE_SENSITIVITIES, FuturesRatesSensitivityCalculator.getInstance(DividendYieldFuturesCalculator.PresentValueCalculator.getInstance()));
  }

  // Need to do this to get labels for the output
  private ValueRequirement getCurveSpecRequirement(final Currency currency, final String curveName) {
    final ValueProperties properties = ValueProperties.builder().with(ValuePropertyNames.CURVE, curveName).get();
    return new ValueRequirement(ValueRequirementNames.YIELD_CURVE_SPEC, ComputationTargetType.PRIMITIVE, currency.getUniqueId(), properties);
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final Set<ValueRequirement> requirements = super.getRequirements(context, target, desiredValue);
    if (requirements == null) {
      return null;
    }
    // Get Funding Curve Name and Configuration
    final String fundingCurveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
    final FutureSecurity security = (FutureSecurity)  target.getTrade().getSecurity();
    // Add Funding Curve Spec, to get labels correct in result
    requirements.add(getCurveSpecRequirement(FinancialSecurityUtils.getCurrency(security), fundingCurveName));
    return requirements;
  }

  @Override
  public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target, final Set<ValueRequirement> desiredValues) {
    // 1. Build the analytic derivative to be priced
    final Trade trade = target.getTrade();
    final FutureSecurity security = (FutureSecurity) trade.getSecurity();

    final HistoricalTimeSeriesBundle timeSeriesBundle = HistoricalTimeSeriesFunctionUtils.getHistoricalTimeSeriesInputs(executionContext, inputs);
    Double lastMarginPrice = null;
    try {
      lastMarginPrice = timeSeriesBundle.get(MarketDataRequirementNames.MARKET_VALUE, security.getExternalIdBundle()).getTimeSeries().getLatestValue();
    } catch (final NoSuchElementException e) {
      throw new OpenGammaRuntimeException("Time series for " + security.getExternalIdBundle() + " was empty");
    }
    final ZonedDateTime valuationTime = executionContext.getValuationClock().zonedDateTime();
    final InstrumentDefinitionWithData<?, Double> definition = security.accept(CONVERTER);
    final InstrumentDerivative derivative = definition.toDerivative(valuationTime, lastMarginPrice);

    // 2. Build up the market data bundle
    final SimpleFutureDataBundle market = getFutureDataBundle(security, inputs, timeSeriesBundle, desiredValues.iterator().next());
    final ValueRequirement desiredValue = desiredValues.iterator().next();
    final YieldCurveBundle curveBundle = new YieldCurveBundle();
    final String fundingCurveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
    curveBundle.setCurve(fundingCurveName, market.getFundingCurve());
    // 3. Create specification that matches the properties promised in getResults
    // Build up InstrumentLabelledSensitivities for the Curve
    final Object curveSpecObject = inputs.getValue(ValueRequirementNames.YIELD_CURVE_SPEC);
    if (curveSpecObject == null) {
      throw new OpenGammaRuntimeException("Curve specification was null");
    }
    final InterpolatedYieldCurveSpecificationWithSecurities curveSpec = (InterpolatedYieldCurveSpecificationWithSecurities) curveSpecObject;

    final ValueSpecification resultSpec = new ValueSpecification(ValueRequirementNames.YIELD_CURVE_NODE_SENSITIVITIES, target.toSpecification(),
        createValueProperties(target, desiredValue).get());

    // 4. Compute sensitivity to the discount rate, then use chain rule to distribute sensitivity across the curve
    final DoubleMatrix1D sensVector = derivative.accept(getCalculator(), getFutureDataBundle(security, inputs, timeSeriesBundle, desiredValue));
    return YieldCurveNodeSensitivitiesHelper.getInstrumentLabelledSensitivitiesForCurve(fundingCurveName, curveBundle, sensVector, curveSpec, resultSpec);
  }
}
