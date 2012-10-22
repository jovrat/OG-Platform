/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.fudgemsg;

import java.util.List;
import java.util.Map;

import javax.time.calendar.LocalDate;

import org.fudgemsg.FudgeField;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeBuilder;
import org.fudgemsg.mapping.FudgeBuilderFor;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opengamma.financial.analytics.ResetScheduleMatrix;
import com.opengamma.util.money.CurrencyAmount;
import com.opengamma.util.tuple.Pair;

/**
 *
 */
@FudgeBuilderFor(ResetScheduleMatrix.class)
public class ResetScheduleMatrixBuilder implements FudgeBuilder<ResetScheduleMatrix> {
  private static final String DATES_FIELD = "dates";
  private static final String PAIRS_FIELD = "pairs";
  private static final String CA_FIELD = "ca";
  private static final String RESET_INDEX_FIELD = "resetIndex";
  private static final String MAX_AMOUNTS_FIELD = "maxAmounts";

  @Override
  public MutableFudgeMsg buildMessage(final FudgeSerializer serializer, final ResetScheduleMatrix object) {
    final MutableFudgeMsg message = serializer.newMessage();
    for (final Map.Entry<LocalDate, List<Pair<CurrencyAmount, String>>> entry : object.getValues().entrySet()) {
      serializer.addToMessageWithClassHeaders(message, DATES_FIELD, null, entry.getKey());
      final MutableFudgeMsg perDateMessage = serializer.newMessage();
      for (final Pair<CurrencyAmount, String> pair : entry.getValue()) {
        serializer.addToMessageWithClassHeaders(perDateMessage, CA_FIELD, null, pair.getFirst());
        perDateMessage.add(RESET_INDEX_FIELD, pair.getSecond());
      }
      message.add(PAIRS_FIELD, perDateMessage);
    }
    message.add(MAX_AMOUNTS_FIELD, object.getMaxEntries());
    return message;
  }

  @Override
  public ResetScheduleMatrix buildObject(final FudgeDeserializer deserializer, final FudgeMsg message) {
    final List<FudgeField> dateFields = message.getAllByName(DATES_FIELD);
    final List<FudgeField> pairsField = message.getAllByName(PAIRS_FIELD);
    final Map<LocalDate, List<Pair<CurrencyAmount, String>>> values = Maps.newHashMapWithExpectedSize(dateFields.size());
    for (int i = 0; i < dateFields.size(); i++) {
      final LocalDate date = deserializer.fieldValueToObject(LocalDate.class, dateFields.get(i));
      final FudgeMsg perDateMessage = (FudgeMsg) pairsField.get(i).getValue();
      final List<FudgeField> caMessage = perDateMessage.getAllByName(CA_FIELD);
      final List<FudgeField> resetIndexMessage = perDateMessage.getAllByName(RESET_INDEX_FIELD);
      final List<Pair<CurrencyAmount, String>> list = Lists.newArrayListWithCapacity(caMessage.size());
      for (int j = 0; j < caMessage.size(); j++) {
        final CurrencyAmount ca = deserializer.fieldValueToObject(CurrencyAmount.class, caMessage.get(j));
        final String resetIndex = (String) resetIndexMessage.get(j).getValue();
        list.add(Pair.of(ca, resetIndex));
      }
      values.put(date, list);
    }
    final int maxAmounts = message.getInt(MAX_AMOUNTS_FIELD);
    return new ResetScheduleMatrix(values, maxAmounts);
  }

}