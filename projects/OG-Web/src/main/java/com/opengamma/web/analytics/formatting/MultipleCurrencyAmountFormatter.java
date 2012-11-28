/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics.formatting;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.CurrencyAmount;
import com.opengamma.util.money.MultipleCurrencyAmount;

/* package */ class MultipleCurrencyAmountFormatter extends AbstractFormatter<MultipleCurrencyAmount> {

  private final DoubleFormatter _doubleFormatter;

  /* package */ MultipleCurrencyAmountFormatter(DoubleFormatter doubleFormatter) {
    super(MultipleCurrencyAmount.class);
    ArgumentChecker.notNull(doubleFormatter, "doubleFormatter");
    _doubleFormatter = doubleFormatter;
    addFormatter(new Formatter<MultipleCurrencyAmount>(Format.EXPANDED) {
      @Override
      Object format(MultipleCurrencyAmount value, ValueSpecification valueSpec) {
        return formatExpanded(value, valueSpec);
      }
    });
  }

  @Override
  public String formatCell(MultipleCurrencyAmount value, ValueSpecification valueSpec) {
    return "Vector (" + value.size() + ")";
  }

  private List<List<String>> formatExpanded(MultipleCurrencyAmount value, ValueSpecification valueSpec) {
    CurrencyAmount[] currencyAmounts = value.getCurrencyAmounts();
    List<List<String>> results = Lists.newArrayListWithCapacity(currencyAmounts.length);
    for (CurrencyAmount currencyAmount : currencyAmounts) {
      String formattedValue = _doubleFormatter.formatCell(currencyAmount.getAmount(), valueSpec);
      List<String> rowResults = ImmutableList.of(currencyAmount.getCurrency().getCode(), formattedValue);
      results.add(rowResults);
    }
    return results;
  }

    @Override
  public DataType getDataType() {
    return DataType.LABELLED_MATRIX_1D;
  }
}
