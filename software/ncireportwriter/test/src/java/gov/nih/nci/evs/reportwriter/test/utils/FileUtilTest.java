/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test.utils;

import gov.nih.nci.evs.reportwriter.utils.*;
import org.apache.log4j.*;

public class FileUtilTest {
    private static Logger _logger = Logger.getLogger(FileUtilTest.class);
    
    public static void main(String[] args) {
        try {
            String dir = "C:/apps/evs/ncireportwriter-webapp/downloads";
            String txtFile = dir + "/CDISC_SDTM_Terminology__09.12d.txt";
            String delimiter = "\t";
            FileUtil.convertToExcel(txtFile, delimiter);
            _logger.debug("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
