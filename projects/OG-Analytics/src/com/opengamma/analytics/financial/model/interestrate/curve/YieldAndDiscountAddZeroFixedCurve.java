/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.model.interestrate.curve;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.opengamma.util.ArgumentChecker;

/**
 * YieldAndDiscountCurve created by adding the zero-coupon continuously compounded rate of two curves. One curve is fixed and there is no sensitivity to that curve.
 * In general the fixed curve represent a static adjustment like a saisonality adjustment. 
 * The term "fixed" for the second curve means that no parameter is associated to that curve.
 */
public class YieldAndDiscountAddZeroFixedCurve extends YieldAndDiscountCurve {

  /**
   * The main underlying curve.
   */
  private final YieldAndDiscountCurve _curve;
  /**
   * The fixed curve.
   */
  private final YieldAndDiscountCurve _curveFixed;
  /**
   * If 1 the rates are added, if -1, they are subtracted (curve - curveFixed).
   */
  private final double _sign;

  /**
   * Constructor from an array of curves. 
   * The new curve interest rate (zero-coupon continuously compounded) will be the sum (or the difference) of the different underlying curves.
   * @param name The curve name.
   * @param substract If true, the rate of all curves, except the first one, will be subtracted from the first one. If false, all the rates are added.
   * @param curve The main curve.
   * @param curveFixed The fixed curve (as a spread).
   */
  public YieldAndDiscountAddZeroFixedCurve(final String name, final boolean substract, final YieldAndDiscountCurve curve, final YieldAndDiscountCurve curveFixed) {
    super(name);
    ArgumentChecker.notNull(curve, "Curve");
    ArgumentChecker.notNull(curveFixed, "Curve fixed");
    _sign = substract ? -1.0 : 1.0;
    _curve = curve;
    _curveFixed = curveFixed;
  }

  @Override
  public double getInterestRate(Double t) {
    return _curve.getInterestRate(t) + _sign * _curveFixed.getInterestRate(t);
  }

  @Override
  public double[] getInterestRateParameterSensitivity(double time) {
    return _curve.getInterestRateParameterSensitivity(time);
  }

  @Override
  public int getNumberOfParameters() {
    return _curve.getNumberOfParameters();
  }

  @Override
  public List<String> getUnderlyingCurvesNames() {
    List<String> names = new ArrayList<String>();
    names.add(_curve.getName());
    names.add(_curveFixed.getName());
    return names;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + _curve.hashCode();
    result = prime * result + _curveFixed.hashCode();
    long temp;
    temp = Double.doubleToLongBits(_sign);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    YieldAndDiscountAddZeroFixedCurve other = (YieldAndDiscountAddZeroFixedCurve) obj;
    if (!ObjectUtils.equals(_curve, other._curve)) {
      return false;
    }
    if (!ObjectUtils.equals(_curveFixed, other._curveFixed)) {
      return false;
    }
    if (Double.doubleToLongBits(_sign) != Double.doubleToLongBits(other._sign)) {
      return false;
    }
    return true;
  }

}