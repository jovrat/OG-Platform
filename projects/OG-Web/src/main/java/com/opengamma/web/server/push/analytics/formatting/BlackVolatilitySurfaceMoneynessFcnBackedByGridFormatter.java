/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.server.push.analytics.formatting;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opengamma.analytics.financial.model.volatility.smile.fitting.sabr.SmileSurfaceDataBundle;
import com.opengamma.analytics.financial.model.volatility.surface.BlackVolatilitySurfaceMoneynessFcnBackedByGrid;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.web.server.conversion.LabelFormatter;

public class BlackVolatilitySurfaceMoneynessFcnBackedByGridFormatter 
    extends AbstractFormatter<BlackVolatilitySurfaceMoneynessFcnBackedByGrid> {

  private final BlackVolatilitySurfaceMoneynessFormatter _delegate;

  /* package */ BlackVolatilitySurfaceMoneynessFcnBackedByGridFormatter(BlackVolatilitySurfaceMoneynessFormatter delegate) {
    super(BlackVolatilitySurfaceMoneynessFcnBackedByGrid.class);
    ArgumentChecker.notNull(delegate, "delegate");
    _delegate = delegate;
    addFormatter(new Formatter<BlackVolatilitySurfaceMoneynessFcnBackedByGrid>(Format.EXPANDED) {
      @Override
      Object format(BlackVolatilitySurfaceMoneynessFcnBackedByGrid value, ValueSpecification valueSpec) {
        return formatExpanded(value);
      }
    });
  }

  @Override
  public Object formatCell(BlackVolatilitySurfaceMoneynessFcnBackedByGrid value, ValueSpecification valueSpec) {
    return _delegate.formatCell(value, valueSpec);
  }

  private Object formatExpanded(BlackVolatilitySurfaceMoneynessFcnBackedByGrid value) {
    SmileSurfaceDataBundle gridData = value.getGridData();
    Set<Double> strikes = new TreeSet<Double>();
    for (double[] outer : gridData.getStrikes()) {
      for (double inner : outer) {
        strikes.add(inner);
      }
    }
    List<Double> vol = Lists.newArrayList();
    // x values expiries
    // y values strikes
    List<Double> expiries = Lists.newArrayListWithCapacity(gridData.getExpiries().length);
    for (double expiry : gridData.getExpiries()) {
      expiries.add(expiry);
    }
    for (Double expiry : expiries) {
      for (Double strike : strikes) {
        vol.add(value.getVolatility(expiry, strike));
      }
    }
    Map<String, Object> results = Maps.newHashMap();
    results.put("x_values", expiries);
    results.put("x_labels", getAxisLabels(expiries));
    results.put("x_title", "Time to Expiry");
    results.put("y_values", strikes);
    results.put("y_labels", getAxisLabels(strikes));
    results.put("y_title", "Strike");
    results.put("vol", vol);
    return results;
  }

  private List<String> getAxisLabels(Collection values) {
    List<String> labels = Lists.newArrayListWithCapacity(values.size());
    for (Object value : values) {
      labels.add(LabelFormatter.format(value));
    }
    return labels;
  }

  @Override
  public DataType getDataType() {
    return DataType.SURFACE_DATA;
  }
}