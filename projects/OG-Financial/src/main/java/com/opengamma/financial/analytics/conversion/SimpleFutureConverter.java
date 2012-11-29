/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.conversion;

import javax.time.calendar.ZonedDateTime;

import org.apache.commons.lang.Validate;

import com.opengamma.analytics.financial.simpleinstruments.definition.SimpleFXFutureDefinition;
import com.opengamma.analytics.financial.simpleinstruments.definition.SimpleFutureDefinition;
import com.opengamma.analytics.financial.simpleinstruments.definition.SimpleInstrumentDefinition;
import com.opengamma.financial.security.FinancialSecurityVisitorAdapter;
import com.opengamma.financial.security.future.AgricultureFutureSecurity;
import com.opengamma.financial.security.future.BondFutureSecurity;
import com.opengamma.financial.security.future.EnergyFutureSecurity;
import com.opengamma.financial.security.future.EquityFutureSecurity;
import com.opengamma.financial.security.future.EquityIndexDividendFutureSecurity;
import com.opengamma.financial.security.future.FXFutureSecurity;
import com.opengamma.financial.security.future.FutureSecurity;
import com.opengamma.financial.security.future.IndexFutureSecurity;
import com.opengamma.financial.security.future.InterestRateFutureSecurity;
import com.opengamma.financial.security.future.MetalFutureSecurity;
import com.opengamma.financial.security.future.StockFutureSecurity;

/**
 *
 */
public class SimpleFutureConverter extends FinancialSecurityVisitorAdapter<SimpleInstrumentDefinition<?>> {

  @Override
  public SimpleInstrumentDefinition<?> visitAgricultureFutureSecurity(final AgricultureFutureSecurity security) {
    return getDefinition(security);
  }

  @Override
  public SimpleInstrumentDefinition<?> visitBondFutureSecurity(final BondFutureSecurity security) {
    throw new UnsupportedOperationException("Cannot use this converter for BondFutureSecurity");
  }

  @Override
  public SimpleInstrumentDefinition<?> visitEnergyFutureSecurity(final EnergyFutureSecurity security) {
    return getDefinition(security);
  }

  @Override
  public SimpleInstrumentDefinition<?> visitEquityFutureSecurity(final EquityFutureSecurity security) {
    return getDefinition(security);
  }

  @Override
  public SimpleInstrumentDefinition<?> visitEquityIndexDividendFutureSecurity(final EquityIndexDividendFutureSecurity security) {
    return getDefinition(security);
  }

  @Override
  public SimpleInstrumentDefinition<?> visitFXFutureSecurity(final FXFutureSecurity security) {
    Validate.notNull(security, "security");
    final ZonedDateTime expiry = security.getExpiry().getExpiry();
    final double referencePrice = 0;
    return new SimpleFXFutureDefinition(expiry, expiry, referencePrice, security.getNumerator(), security.getDenominator(), security.getUnitAmount());
  }

  @Override
  public SimpleInstrumentDefinition<?> visitIndexFutureSecurity(final IndexFutureSecurity security) {
    return getDefinition(security);
  }

  @Override
  public SimpleInstrumentDefinition<?> visitInterestRateFutureSecurity(final InterestRateFutureSecurity security) {
    throw new UnsupportedOperationException("Cannot use this converter for InterestRateFutureSecurity");
  }

  @Override
  public SimpleInstrumentDefinition<?> visitMetalFutureSecurity(final MetalFutureSecurity security) {
    return getDefinition(security);
  }

  @Override
  public SimpleInstrumentDefinition<?> visitStockFutureSecurity(final StockFutureSecurity security) {
    return getDefinition(security);
  }

  private SimpleInstrumentDefinition<?> getDefinition(final FutureSecurity security) {
    return getDefinition(security, 0.0);
  }

  private SimpleInstrumentDefinition<?> getDefinition(final FutureSecurity security, final double referencePrice) {
    Validate.notNull(security, "security");
    final ZonedDateTime expiry = security.getExpiry().getExpiry();
    return new SimpleFutureDefinition(expiry, expiry, referencePrice, security.getCurrency(), security.getUnitAmount());
  }


}
