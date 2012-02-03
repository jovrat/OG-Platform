/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.bond;

import java.util.Collections;
import java.util.Set;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.financial.property.DefaultPropertyFunction;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.bond.BondSecurity;
import com.opengamma.util.ArgumentChecker;

/**
 * 
 */
public class BondDefaultCurveNamesFunction extends DefaultPropertyFunction {
  private final String[] _valueNames;
  private final String _riskFreeCurve;
  private final String _creditCurve;

  public BondDefaultCurveNamesFunction(final String riskFreeCurve, final String creditCurve, final String... valueNames) {
    super(ComputationTargetType.SECURITY, true);
    ArgumentChecker.notNull(riskFreeCurve, "risk-free curve name");
    ArgumentChecker.notNull(creditCurve, "credit curve name");
    ArgumentChecker.notNull(valueNames, "value names");
    _riskFreeCurve = riskFreeCurve;
    _creditCurve = creditCurve;
    _valueNames = valueNames;
  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    if (!(target.getSecurity() instanceof FinancialSecurity)) {
      return false;
    }
    return target.getSecurity() instanceof BondSecurity;
  }

  @Override
  protected void getDefaults(final PropertyDefaults defaults) {
    for (final String valueName : _valueNames) {
      defaults.addValuePropertyName(valueName, BondFunction.PROPERTY_CREDIT_CURVE);
      defaults.addValuePropertyName(valueName, BondFunction.PROPERTY_RISK_FREE_CURVE);
    }
  }

  @Override
  protected Set<String> getDefaultValue(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue, final String propertyName) {
    if (BondFunction.PROPERTY_CREDIT_CURVE.equals(propertyName)) {
      return Collections.singleton(_creditCurve);
    }
    if (BondFunction.PROPERTY_RISK_FREE_CURVE.equals(propertyName)) {
      return Collections.singleton(_riskFreeCurve);
    }
    return null;
  }
}
