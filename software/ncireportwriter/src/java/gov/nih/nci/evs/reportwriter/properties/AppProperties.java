package gov.nih.nci.evs.reportwriter.properties;

import gov.nih.nci.evs.properties.*;

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

public class AppProperties extends PropertyNameValueFileParser {
    // -------------------------------------------------------------------------
    public static final String BUILD_INFO = "BUILD_INFO";
    public static final String EVS_SERVICE_URL = "EVS_SERVICE_URL";
    public static final String APPLICATION_VERSION = "APPLICATION_VERSION";
    public static final String ANTHILL_BUILD_TAG_BUILT = "ANTHILL_BUILD_TAG_BUILT";      
    public static final String MAIL_SMTP_SERVER = "MAIL_SMTP_SERVER";
    public static final String DEBUG_ON = "DEBUG_ON";
    public static final String REPORT_DOWNLOAD_DIRECTORY = "REPORT_DOWNLOAD_DIRECTORY";
    public static final String MAXIMUM_LEVEL = "MAXIMUM_LEVEL";
    public static final String MAXIMUM_RETURN = "MAXIMUM_RETURN";
    public static final String CSM_LOCKOUT_TIME = "CSM_LOCKOUT_TIME";
    public static final String CSM_ALLOWED_LOGIN_TIME = "CSM_ALLOWED_LOGIN_TIME";
    public static final String CSM_ALLOWED_ATTEMPTS = "CSM_ALLOWED_ATTEMPTS";
    public static final String CONTACT_US_EMAIL = "CONTACT_US_EMAIL";
    public static final String ACCOUNT_ADMIN_USER_EMAIL = "ACCOUNT_ADMIN_USER_EMAIL";
    
    // -------------------------------------------------------------------------
    private final String PROPERTY_FILE_ENV =
        "gov.nih.nci.cacore.ncireportwriterProperties";
    private static AppProperties _instance;

    // -------------------------------------------------------------------------
    private AppProperties() {
        String propertyFile = System.getProperty(PROPERTY_FILE_ENV);
        loadProperties(propertyFile);
    }

    public static AppProperties getInstance() {
        if (_instance == null) {
            synchronized (AppProperties.class) {
                _instance = new AppProperties();
            }
        }
        return _instance;
    }
}
