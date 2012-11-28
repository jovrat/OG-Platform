/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.bbg.id;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.opengamma.bbg.BloombergContractID;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.id.ExternalId;

/**
 * Tests {@link DefaultGMIBloombergIDResolver}
 */

@Test
public class DefaultGMIBloombergIDResolverTest {
  
  private static final BloombergContractID BBG_RA_CURNCY = new BloombergContractID("RA", "Curncy");
  private static final BloombergContractID BBG_AC_CURNCY = new BloombergContractID("AC", "Curncy");
  private static final BloombergContractID BBG_TB_CMDTY = new BloombergContractID("TB", "Comdty");
  private static final BloombergContractID BBG_EM_CMDTY = new BloombergContractID("EM", "Comdty");
  private static final ExternalId GMI_02930 = ExternalSchemes.gmiSecurityId("02930");
  private static final ExternalId GMI_16RAO = ExternalSchemes.gmiSecurityId("16RAO");
  private static final ExternalId GMI_16ACF = ExternalSchemes.gmiSecurityId("16ACF");
  private static final ExternalId GMI_16T1F = ExternalSchemes.gmiSecurityId("16T1F");
  private static final ExternalId GMI_16EMO = ExternalSchemes.gmiSecurityId("16EMO");
  private static final ExternalId GMI_16EMF = ExternalSchemes.gmiSecurityId("16EMF");
  private static GMIBloombergIDResolver s_idResolver;

  @BeforeClass
  public void setUp() throws URISyntaxException {
    URL mappingFileUrl = DefaultGMIBloombergIDResolverTest.class.getResource("CME_ExportContracts.csv");
    DefaultGMIBloombergIDResolver idResolver = DefaultGMIBloombergIDResolver.getInstance();
    idResolver.setMappingFiles(Collections.singletonList(new File(mappingFileUrl.toURI())));
    s_idResolver = idResolver;
  }
  
  public void resolveSingle() {
    assertEquals(BBG_EM_CMDTY, s_idResolver.resolve(GMI_16EMF));
    assertEquals(BBG_EM_CMDTY, s_idResolver.resolve(GMI_16EMO));
    assertEquals(BBG_TB_CMDTY, s_idResolver.resolve(GMI_16T1F));
    assertNull(s_idResolver.resolve(GMI_02930));
    assertEquals(BBG_AC_CURNCY, s_idResolver.resolve(GMI_16ACF));
    assertEquals(BBG_RA_CURNCY, s_idResolver.resolve(GMI_16RAO));
  }
  
  public void resolveMultiples() {
    Map<ExternalId, BloombergContractID> resultMap = s_idResolver.resolve(Lists.newArrayList(GMI_16EMF, GMI_16EMO, GMI_16T1F, GMI_02930, GMI_16ACF, GMI_16RAO));
    assertEquals(BBG_EM_CMDTY, resultMap.get(GMI_16EMF));
    assertEquals(BBG_EM_CMDTY, resultMap.get(GMI_16EMO));
    assertEquals(BBG_TB_CMDTY, resultMap.get(GMI_16T1F));
    assertNull(resultMap.get(GMI_02930));
    assertEquals(BBG_AC_CURNCY, resultMap.get(GMI_16ACF));
    assertEquals(BBG_RA_CURNCY, resultMap.get(GMI_16RAO));
  }
  
}
