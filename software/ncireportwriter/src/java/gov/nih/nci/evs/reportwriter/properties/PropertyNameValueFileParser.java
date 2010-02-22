package gov.nih.nci.evs.reportwriter.properties;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

import gov.nih.nci.evs.reportwriter.utils.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

public class PropertyNameValueFileParser {
    private static Logger _logger = Logger
        .getLogger(PropertyNameValueFileParser.class);
    private Properties _properties = new Properties();

    protected void loadProperties(String propertyFile) {
        try {
            _logger.info("File location= " + propertyFile);
            if (propertyFile == null || propertyFile.length() <= 0)
                throw new Exception("Property file not set." + 
                    "\n  * Property File: " + propertyFile);
    
            FileInputStream fis = new FileInputStream(new File(propertyFile));
            _properties.load(fis);
            debugProperties();
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
        }
    }
    
    protected String fetchProperty(String key) {
        return _properties.getProperty(key);
    }

    protected String getSpecialProperty(String key) {
        String value = "";
        try {
            value = getProperty(key);
            if (value == null)
                value = "null";
        } catch (Exception e) {
            value = e.getMessage();
        }

        _logger.info(key + ": " + value);
        return value;
    }

    public String getProperty(String key) {
        return fetchProperty(key);
    }

    public int getIntProperty(String key, int defaultValue) {
        String strValue = fetchProperty(key);
        try {
            if (strValue == null)
                return defaultValue;
            return Integer.parseInt(strValue);
        } catch (Exception e) {
            _logger.error("Invalid integer property value for: " + key);
            _logger.error("  Value from the property file: " + strValue);
            _logger.error("  Defaulting to: " + defaultValue);
            return defaultValue;
        }
    }

    public boolean getBoolProperty(String key, boolean defaultValue) {
        String strValue = fetchProperty(key);
        try {
            if (strValue == null)
                return defaultValue;
            return Boolean.parseBoolean(strValue);
        } catch (Exception e) {
            _logger.error("Invalid boolean property value for: " + key);
            _logger.error("  Value from the property file: " + strValue);
            _logger.error("  Defaulting to: " + defaultValue);
            return defaultValue;
        }
    }

    private void debugProperties() {
        if (! _logger.isDebugEnabled())
            return;

        ArrayList<String> keys = new ArrayList<String>();
        Iterator<?> iterator = _properties.keySet().iterator();
        while (iterator.hasNext())
            keys.add((String) iterator.next());
        SortUtils.quickSort(keys);
        
        _logger.debug("List of properties:");
        for (int i=0; i<keys.size(); ++i) {
            String key = keys.get(i);
            String value = _properties.getProperty(key);
            _logger.debug("  " + i + ") " + key + ": " + value);
        }
    }
}
