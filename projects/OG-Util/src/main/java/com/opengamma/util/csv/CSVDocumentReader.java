/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.util.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;

/**
 * Provides an iterator for reading a CSV file that return each row as a fudge message
 * <p>
 * The first row of the CSV document is assumed to be the column headers.
 * The FudgeMessage returned by the iterator has the column headers as field names
 */
public final class CSVDocumentReader implements Iterable<FudgeMsg> {
  
  private static final FudgeContext s_fudgeContext = OpenGammaFudgeContext.getInstance();
  
  private URL _docUrl;
  private char _separator;
  private char _quotechar; 
  private char _escape;
  
  /**
   * Constructs CSVDocumentReader using a comma for the separator.
   * 
   * @param docUrl the URL to the CSV source.
   */
  public CSVDocumentReader(URL docUrl) {
    this(docUrl, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER);
  }
  
  /**
   * Constructs CSVDocumentReader with supplied separator.
   * 
   * @param docUrl the URL to the CSV source.
   * @param separator the delimiter to use for separating entries.
   */
  public CSVDocumentReader(URL docUrl, char separator) {
      this(docUrl, separator, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER);
  }

  /**
   * Constructs CSVDocumentReader with supplied separator and quote char.
   * 
   * @param docUrl the URL to the CSV source.
   * @param separator the delimiter to use for separating entries
   * @param quotechar the character to use for quoted elements
   */
  public CSVDocumentReader(URL docUrl, char separator, char quotechar) {
      this(docUrl, separator, quotechar, CSVParser.DEFAULT_ESCAPE_CHARACTER);
  }
  
  /**
   * Constructs CSVDocumentReader with supplied separator, quote char and escape char.
   *
   * @param docUrl the URL to the CSV source.
   * @param separator the delimiter to use for separating entries
   * @param quotechar the character to use for quoted elements
   * @param escape the character to use for escaping a separator or quote
   */
  public CSVDocumentReader(URL docUrl, char separator, char quotechar, char escape) {
    ArgumentChecker.notNull(docUrl, "file");
    ArgumentChecker.notNull(separator, "separator");
    ArgumentChecker.notNull(quotechar, "quotechar");
    ArgumentChecker.notNull(escape, "escape");
    _docUrl = docUrl;
    _separator = separator;
    _quotechar = quotechar;
    _escape = escape;
  }
  
  @Override
  public Iterator<FudgeMsg> iterator() {
    return new FudgeMsgCSVIterator();
  }
    
  private class FudgeMsgCSVIterator implements Iterator<FudgeMsg> {
    
    private CSVReader _csvReader;
    private String[] _header;
    private String[] _currentRow;
    
    public FudgeMsgCSVIterator() {
      try {
        _csvReader = new CSVReader(new BufferedReader(new InputStreamReader(_docUrl.openStream())), _separator, _quotechar, _escape);
        _header = _csvReader.readNext();
        if (_header == null) {
          throw new OpenGammaRuntimeException("Column headers is missing, can not create iterator");
        } else {
          trimColumnHeaders();
        }
      } catch (IOException ex) {
        throw new OpenGammaRuntimeException("IO Exception trying to create an Iterator", ex);
      }
    }

    private void trimColumnHeaders() {
      for (int i = 0; i < _header.length; i++) {
        _header[i] = StringUtils.trim(_header[i]);
      }
    }

    @Override
    public boolean hasNext() {
      try {
        _currentRow = _csvReader.readNext();
      } catch (IOException ex) {
        throw new OpenGammaRuntimeException("IO Exception trying to read next row", ex);
      }
      if (_currentRow == null) {
        IOUtils.closeQuietly(_csvReader);
        _csvReader = null;
      }
      return _currentRow != null;
    }

    @Override
    public FudgeMsg next() {
      MutableFudgeMsg currentMsg = s_fudgeContext.newMessage();
      for (int i = 0; i < _header.length; i++) {
        try {
          String currentRow = StringUtils.trimToNull(_currentRow[i]);
          if (currentRow != null) {
            currentMsg.add(_header[i], currentRow);
          }
        } catch (ArrayIndexOutOfBoundsException ex) {
          // do nothing, current row has less columns compared to header
        }
      }
      return currentMsg;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Cannot remove CSV row");
    }

    @Override
    protected void finalize() throws Throwable {
      //close file inputstream if it is still hanging around
      if (_csvReader != null) {
        IOUtils.closeQuietly(_csvReader);
      }
    }
  }

}
