/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.future;

import com.opengamma.analytics.financial.future.CostOfCarryFuturesCalculator;
import com.opengamma.engine.value.ValueRequirementNames;

/**
 *
 */
public class CostOfCarrySpotFuturesFunction extends CostOfCarryFuturesFunction<Double> {

  public CostOfCarrySpotFuturesFunction() {
    super(ValueRequirementNames.SPOT, CostOfCarryFuturesCalculator.SpotPriceCalculator.getInstance());
  }

}