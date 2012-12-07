/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.commodity.calculation;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.opengamma.analytics.financial.commodity.calculator.CommodityFutureOptionBlackForwardDeltaCalculator;
import com.opengamma.analytics.financial.commodity.definition.AgricultureFutureDefinition;
import com.opengamma.analytics.financial.commodity.definition.EnergyFutureDefinition;
import com.opengamma.analytics.financial.commodity.definition.MetalFutureDefinition;
import com.opengamma.analytics.financial.commodity.definition.SettlementType;
import com.opengamma.analytics.financial.commodity.derivative.AgricultureFuture;
import com.opengamma.analytics.financial.commodity.derivative.AgricultureFutureOption;
import com.opengamma.analytics.financial.commodity.derivative.EnergyFuture;
import com.opengamma.analytics.financial.commodity.derivative.EnergyFutureOption;
import com.opengamma.analytics.financial.commodity.derivative.MetalFuture;
import com.opengamma.analytics.financial.commodity.derivative.MetalFutureOption;
import com.opengamma.util.money.Currency;

/**
 * Checks the wiring of the  CommodityFutureOptionPresentValueCalculator
 */
public class CommodityFutureOptionForwardDeltaCalculatorTest extends CommodityFutureOptionTestDefaults {

  private static final CommodityFutureOptionBlackForwardDeltaCalculator PRICER = CommodityFutureOptionBlackForwardDeltaCalculator.getInstance();

  @Test
  public void testAgricultureFutureOption() {
    final double answer = 4.415360077156871;

    final AgricultureFutureDefinition definition = new AgricultureFutureDefinition(EXPIRY_DATE, AN_UNDERLYING, UNIT_AMOUNT, null, null, AMOUNT, "tonnes", SettlementType.CASH, 0, Currency.GBP,
        SETTLEMENT_DATE);
    final AgricultureFuture future = definition.toDerivative(A_DATE, new String[0]);
    final AgricultureFutureOption option = new AgricultureFutureOption(EXPIRY, future, STRIKE, EXERCISE, true);
    final double pv = option.accept(PRICER, MARKET);
    assertEquals(answer, pv, TOLERANCE);
  }

  @Test
  public void testEnergyFutureOption() {
    final double answer = 4.415360077156871;

    final EnergyFutureDefinition definition = new EnergyFutureDefinition(EXPIRY_DATE, AN_UNDERLYING, UNIT_AMOUNT, null, null, AMOUNT, "tonnes", SettlementType.CASH, 0, Currency.GBP,
        SETTLEMENT_DATE);
    final EnergyFuture future = definition.toDerivative(A_DATE, new String[0]);
    final EnergyFutureOption option = new EnergyFutureOption(EXPIRY, future, STRIKE, EXERCISE, true);
    final double pv = option.accept(PRICER, MARKET);
    assertEquals(answer, pv, TOLERANCE);
  }

  @Test
  public void testMetalFutureOption() {
    final double answer = 4.415360077156871;

    final MetalFutureDefinition definition = new MetalFutureDefinition(EXPIRY_DATE, AN_UNDERLYING, UNIT_AMOUNT, null, null, AMOUNT, "tonnes", SettlementType.CASH, 0, Currency.GBP,
        SETTLEMENT_DATE);
    final MetalFuture future = definition.toDerivative(A_DATE, new String[0]);
    final MetalFutureOption option = new MetalFutureOption(EXPIRY, future, STRIKE, EXERCISE, true);
    final double pv = option.accept(PRICER, MARKET);
    assertEquals(answer, pv, TOLERANCE);
  }
}
