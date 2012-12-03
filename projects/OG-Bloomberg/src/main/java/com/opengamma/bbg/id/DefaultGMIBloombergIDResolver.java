/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.bbg.id;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.fudgemsg.FudgeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opengamma.bbg.BloombergContractID;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.csv.CSVDocumentReader;

/**
 * Default implementation of GMIBloombergIDResolver
 */
public final class DefaultGMIBloombergIDResolver implements GMIBloombergIDResolver {
  
  private static final Logger s_logger = LoggerFactory.getLogger(DefaultGMIBloombergIDResolver.class);
 
  private static final String GMI_HEADER = "GMI";
  private static final String BLOOMBERG_HEADER = "Bloomberg";
  
  /**
   * Singleton instance.
   */
  private static final DefaultGMIBloombergIDResolver s_instance = new DefaultGMIBloombergIDResolver();
  
  private Collection<File> _mappingFiles = Lists.newArrayList();  
  private Map<ExternalId, BloombergContractID> _jpm2BloombergTicker = Maps.newHashMap();
  
  /**
   * Restricted constructor.
   */
  private DefaultGMIBloombergIDResolver() {
  }

  /**
   * Return the bloomberg security ticker for a corresponding GMI contractCode and exchangeCode.
   * @param gmiCode the gmiCode in the format exchangeCode and contractCode eg PAAH2.
   * @return the matching bloomberg contract id.
   */
  @Override
  public BloombergContractID resolve(ExternalId gmiCode) {
    ArgumentChecker.notNull(gmiCode, "gmiCode");
    return _jpm2BloombergTicker.get(gmiCode);
  }
  
  @Override
  public Map<ExternalId, BloombergContractID> resolve(Collection<ExternalId> specs) {
    Map<ExternalId, BloombergContractID> result = Maps.newHashMap();
    for (ExternalId  gmiCode : specs) {
      result.put(gmiCode, resolve(gmiCode));
    }
    return result;
  }

  /**
   * Gets the mappingFiles.
   * @return the mappingFiles
   */
  public Collection<File> getMappingFiles() {
    return ImmutableList.copyOf(_mappingFiles);
  }

  /**
   * Sets the mappingFiles.
   * <p>
   * The expected mapping files are comma separated csv files, with GMI and Bloomberg column headers for the gmicodes to bloomberg contract ids respectively
   *
   * @param mappingFiles  the mappingFiles
   */
  public void setMappingFiles(Collection<File> mappingFiles) {
    ArgumentChecker.notNull(mappingFiles, "mapping files");
    resetMappingTable();
    _mappingFiles.addAll(mappingFiles);
    buildMappingTable();
  }

  private void resetMappingTable() {
    _mappingFiles.clear();
    _jpm2BloombergTicker.clear();
  }
  
  private void buildMappingTable() {
    Map<File, Map<ExternalId, BloombergContractID>> filePerMapping = Maps.newHashMap();
    for (File mappingFile : _mappingFiles) {
      Map<ExternalId, BloombergContractID> gmiMapping = filePerMapping.get(mappingFile);
      if (gmiMapping == null) {
        gmiMapping = Maps.newHashMap();
        filePerMapping.put(mappingFile, gmiMapping);
      }
      try {
        CSVDocumentReader csvDocumentReader = new CSVDocumentReader(mappingFile.toURI().toURL());
        for (FudgeMsg row : csvDocumentReader) {
          String bloombergID = row.getString(BLOOMBERG_HEADER);
          if (isValidID(bloombergID)) {
            String[] bbgIdParts = StringUtils.split(bloombergID);
            ExternalId gmiSecurityId = ExternalSchemes.gmiSecurityId(row.getString(GMI_HEADER));
            BloombergContractID bbgContractID = new BloombergContractID(bbgIdParts[0], bbgIdParts[1]);
            _jpm2BloombergTicker.put(gmiSecurityId, bbgContractID);
            gmiMapping.put(gmiSecurityId, bbgContractID);
          }
        }
      } catch (MalformedURLException ex) {
        s_logger.warn("Error reading mapping file " + mappingFile.getAbsolutePath(), ex);
      }
    }
    if (s_logger.isDebugEnabled()) {
      printMappingTable(filePerMapping);
    }
  }

  private void printMappingTable(Map<File, Map<ExternalId, BloombergContractID>> filePerMapping) {
    for (Entry<File, Map<ExternalId, BloombergContractID>> entry : filePerMapping.entrySet()) {
      File mappingFile = entry.getKey();
      Map<ExternalId, BloombergContractID> gmiMapping = entry.getValue();
      StringBuilder buf = new StringBuilder();
      for (Entry<ExternalId, BloombergContractID> gmiEntry : gmiMapping.entrySet()) {
        buf.append("\t").append(gmiEntry.getKey().getValue()).append(":").append(gmiEntry.getValue().getContractCode()).append(" ").append(gmiEntry.getValue().getMarketSector()).append("\n");
      }
      s_logger.debug("mappings for {}\n{}\n", mappingFile.getAbsolutePath(), buf.toString());
    }
  }

  private boolean isValidID(String bloombergID) {
    return bloombergID != null && !"Not Available".equalsIgnoreCase(bloombergID);
  }

  /**
   * Gets the singleton instance of the JPMBloombergIDResolverImpl.
   * 
   * @return the singleton instance, not null
   */
  public static DefaultGMIBloombergIDResolver getInstance() {
    return s_instance;
  }

}
