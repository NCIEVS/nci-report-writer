/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.properties;

import gov.nih.nci.evs.properties.*;

/**
 *
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class AppProperties extends PropertyFileParser {
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
    public static final String NCIT_URL = "NCIT_URL";
    public static final String DISPLAY_NCIT_CODE_URL = "DISPLAY_NCIT_CODE_URL";

    public static final String NCIT_VERSION = "NCIT_VERSION";

    // -------------------------------------------------------------------------
    private final String PROPERTY_FILE_ENV =
        "gov.nih.nci.cacore.ncireportwriterProperties";
    private static AppProperties _instance;

    private static String defautlPropertyFile = "ncireportwriter.properties";

    // -------------------------------------------------------------------------
    private AppProperties() {
        String propertyFile = System.getProperty(PROPERTY_FILE_ENV);
        if (propertyFile == null) {
			propertyFile = defautlPropertyFile;
		}

		System.out.println("loading " + propertyFile);

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

    private String filterOutComments(String text) {
        if (text != null && text.contains("#")) {
            int i = text.indexOf("#");
            text = text.substring(0, i).trim();
        }
        return text;
    }

    public String getProperty(String key) {
        String value = super.getProperty(key);
        value = filterOutComments(value);
        return value;
    }
}
