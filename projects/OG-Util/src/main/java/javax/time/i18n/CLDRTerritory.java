/*
 *  Copyright 2006 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Ported to JSR-310 by Jim Moores
 */
package javax.time.i18n;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.time.calendar.TimeZone;

//CSOFF
/**
 * Provides time data for a specific territory, typically a country.
 * <p>
 * Many pieces of data used in dates and times varies by territory.
 * This class provides access to that data.
 */
public class CLDRTerritory extends Territory {

    /** The raw CLDR data. */
    private static final byte[] cRawData = loadRawData();

    /** The territory id, as per CLDR. */
    private String iID;
    /** The zones. */
    private TimeZone[] iZones;
    /** The first day of week. */
    private int iFirstDOW;
    /** The first day of the business week. */
    private int iFirstBusinessDOW;
    /** The last day of the business week. */
    private int iLastBusinessDOW;
    /** The first day of the weekend. */
    private int iFirstWeekendDOW;
    /** The last day of the weekend. */
    private int iLastWeekendDOW;

    //-----------------------------------------------------------------------
    /**
     * Loads the data from file.
     *
     * @return the array of bytes
     */
    private static byte[] loadRawData() {
        String path = "org/joda/time/contrib/i18n/CLDRTerritoryData.dat";
        try (InputStream baseStream = CLDRTerritory.class.getClassLoader().getResourceAsStream(path)) {
            if (baseStream == null) {
                throw new IOException("Resource not found " + path);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            byte[] bytes = new byte[1024];
            int result = 0;
            while (result != -1) {
                baos.write(bytes, 0, result);
                result = baseStream.read(bytes, 0, 1024);
            }
            return baos.toByteArray();
            
        } catch (IOException ex) {
            throw new IllegalArgumentException("Territory data could not be loaded: " + ex.getMessage());
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param id  the territory id, not null
     */
    CLDRTerritory(String id) {
        super();
        init(id);
    }

    //-----------------------------------------------------------------------
    /**
     * Initialises the data for this id.
     *
     * @param id  the territory id, not null
     * @throws IOException if an error occurs
     */
    private void init(String id) {
        try {
            // open the file
            ByteArrayInputStream bais = new ByteArrayInputStream(cRawData);
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bais));
            byte version = in.readByte();
            if (version != 1) {
                throw new IllegalArgumentException("Unknown file version: " + version);
            }
            
            // loop through file to find the id
            String inputId = in.readUTF();
            while (inputId.equals(id) == false) {
                byte zoneCount = in.readByte();
                for (int i = 0; i < zoneCount; i++) {
                    in.readUTF();
                }
                in.skip(5);
                inputId = in.readUTF();
                if (inputId.length() == 0) {
                    throw new IllegalArgumentException("Territory " + id + " could not be loaded");
                }
            }
            
            // found matching id
            iID = id;
            byte zoneCount = in.readByte();
            iZones = new TimeZone[zoneCount];
            for (int i = 0; i < zoneCount; i++) {
                String zoneID = in.readUTF();
                try {
                    iZones[i] = TimeZone.of(zoneID);
                } catch (IllegalArgumentException ex) {
                    // ignore unless primary, allowing different timezone data files to work
                    if (i == 0) {
                        throw ex;
                    }
                }
            }
            iFirstDOW = in.readByte();
            iFirstBusinessDOW = in.readByte();
            iLastBusinessDOW = in.readByte();
            iFirstWeekendDOW = in.readByte();
            iLastWeekendDOW = in.readByte();
            
        } catch (IOException ex) {
            throw new IllegalArgumentException("Territory " + id + " could not be loaded: " + ex.getMessage());
        }
    }

    //-----------------------------------------------------------------------
    public String getID() {
        return iID;
    }

    //-----------------------------------------------------------------------
	public TimeZone[] getZones() {
        return (TimeZone[]) iZones.clone();
    }

    public int getFirstDayOfWeek() {
        return iFirstDOW;
    }

    public int getBusinessWeekStart() {
        return iFirstBusinessDOW;
    }

    public int getBusinessWeekEnd() {
        return iLastBusinessDOW;
    }

    public int getWeekendStart() {
        return iFirstWeekendDOW;
    }

    public int getWeekendEnd() {
        return iLastWeekendDOW;
    }

}
