/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test.lexevs;

import java.util.*;

import gov.nih.nci.evs.reportwriter.test.utils.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import org.apache.log4j.*;

public class DataUtilsTest2 {
    private static Logger _logger = Logger.getLogger(DataUtilsTest2.class);

    public static void main(String[] args) {
        args = SetupEnv.getInstance().parse(args);
        DataUtilsTest2 test = new DataUtilsTest2();
        test.getSupportedAssociationNames();
    }
    
    public void getSupportedAssociationNames()
    {
        String scheme = "NCI Thesaurus";
        String version = "10.10d";

        Vector<String> values;
        try {
            values = DataUtils.getSupportedAssociations(
                DataUtils.AssociationType.Codes, scheme, version);
            Iterator<String> iterator = values.iterator();
            int i=0;
            while (iterator.hasNext()) {
                String value = iterator.next();
                _logger.info(i + ") " + value);
                ++i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
