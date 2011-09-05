/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.swap;

import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeBuilder;
import org.fudgemsg.mapping.FudgeBuilderFor;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;

import com.opengamma.financial.security.FinancialSecurityFudgeBuilder;
import com.opengamma.util.fudgemsg.AbstractFudgeBuilder;
import com.opengamma.util.time.ZonedDateTimeFudgeBuilder;

/**
 * A Fudge builder for {@code SwapSecurity}.
 */
@FudgeBuilderFor(SwapSecurity.class)
public class SwapSecurityFudgeBuilder extends AbstractFudgeBuilder implements FudgeBuilder<SwapSecurity> {

  /** Field name. */
  public static final String TRADE_DATE_KEY = "tradeDate";
  /** Field name. */
  public static final String EFFECTIVE_DATE_KEY = "effectiveDate";
  /** Field name. */
  public static final String MATURITY_DATE_KEY = "maturityDate";
  /** Field name. */
  public static final String COUNTERPARTY_KEY = "counterparty";
  /** Field name. */
  public static final String PAY_LEG_KEY = "payLeg";
  /** Field name. */
  public static final String RECEIVE_LEG_KEY = "receiveLeg";

  @Override
  public MutableFudgeMsg buildMessage(FudgeSerializer serializer, SwapSecurity object) {
    final MutableFudgeMsg msg = serializer.newMessage();
    SwapSecurityFudgeBuilder.toFudgeMsg(serializer, object, msg);
    return msg;
  }

  public static void toFudgeMsg(FudgeSerializer serializer, SwapSecurity object, final MutableFudgeMsg msg) {
    FinancialSecurityFudgeBuilder.toFudgeMsg(serializer, object, msg);
    addToMessage(msg, TRADE_DATE_KEY, ZonedDateTimeFudgeBuilder.toFudgeMsg(serializer, object.getTradeDate()));
    addToMessage(msg, EFFECTIVE_DATE_KEY, ZonedDateTimeFudgeBuilder.toFudgeMsg(serializer, object.getEffectiveDate()));
    addToMessage(msg, MATURITY_DATE_KEY, ZonedDateTimeFudgeBuilder.toFudgeMsg(serializer, object.getMaturityDate()));
    addToMessage(msg, COUNTERPARTY_KEY, object.getCounterparty());
    addToMessage(serializer, msg, PAY_LEG_KEY, object.getPayLeg(), SwapLeg.class);
    addToMessage(serializer, msg, RECEIVE_LEG_KEY, object.getReceiveLeg(), SwapLeg.class);
  }

  @Override
  public SwapSecurity buildObject(FudgeDeserializer deserializer, FudgeMsg msg) {
    SwapSecurity object = new SwapSecurity();
    SwapSecurityFudgeBuilder.fromFudgeMsg(deserializer, msg, object);
    return object;
  }

  public static void fromFudgeMsg(FudgeDeserializer deserializer, FudgeMsg msg, SwapSecurity object) {
    FinancialSecurityFudgeBuilder.fromFudgeMsg(deserializer, msg, object);
    object.setTradeDate(ZonedDateTimeFudgeBuilder.fromFudgeMsg(deserializer, msg.getMessage(TRADE_DATE_KEY)));
    object.setEffectiveDate(ZonedDateTimeFudgeBuilder.fromFudgeMsg(deserializer, msg.getMessage(EFFECTIVE_DATE_KEY)));
    object.setMaturityDate(ZonedDateTimeFudgeBuilder.fromFudgeMsg(deserializer, msg.getMessage(MATURITY_DATE_KEY)));
    object.setCounterparty(msg.getString(COUNTERPARTY_KEY));
    object.setPayLeg(deserializer.fudgeMsgToObject(SwapLeg.class, msg.getMessage(PAY_LEG_KEY)));
    object.setReceiveLeg(deserializer.fudgeMsgToObject(SwapLeg.class, msg.getMessage(RECEIVE_LEG_KEY)));
  }

}
