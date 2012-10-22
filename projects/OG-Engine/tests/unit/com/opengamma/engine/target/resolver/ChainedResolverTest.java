/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.target.resolver;

import static org.testng.Assert.assertEquals;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.money.Currency;

/**
 * Test the {@link ChainedResolver} class
 */
@Test
public class ChainedResolverTest {

  @SuppressWarnings("unchecked")
  public void testFirst() {
    final ObjectResolver first = Mockito.mock(ObjectResolver.class);
    final ObjectResolver second = Mockito.mock(ObjectResolver.class);
    final ObjectResolver chained = ChainedResolver.CREATE.execute(second, first);
    Mockito.when(first.resolve(UniqueId.of("Foo", "1"), VersionCorrection.LATEST)).thenReturn(Currency.USD);
    Mockito.when(second.resolve(UniqueId.of("Foo", "1"), VersionCorrection.LATEST)).thenReturn(Currency.GBP);
    assertEquals(chained.resolve(UniqueId.of("Foo", "1"), VersionCorrection.LATEST), Currency.USD);
    Mockito.verifyZeroInteractions(second);
  }

  @SuppressWarnings("unchecked")
  public void testSecond() {
    final ObjectResolver first = Mockito.mock(ObjectResolver.class);
    final ObjectResolver second = Mockito.mock(ObjectResolver.class);
    final ObjectResolver chained = ChainedResolver.CREATE.execute(second, first);
    Mockito.when(first.resolve(UniqueId.of("Foo", "1"), VersionCorrection.LATEST)).thenReturn(null);
    Mockito.when(second.resolve(UniqueId.of("Foo", "1"), VersionCorrection.LATEST)).thenReturn(Currency.GBP);
    assertEquals(chained.resolve(UniqueId.of("Foo", "1"), VersionCorrection.LATEST), Currency.GBP);
  }
  
}