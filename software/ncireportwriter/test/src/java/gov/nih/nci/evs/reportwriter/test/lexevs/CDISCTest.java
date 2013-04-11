/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test.lexevs;

import gov.nih.nci.evs.reportwriter.service.*;
import gov.nih.nci.evs.reportwriter.test.utils.*;

import org.apache.log4j.*;

public class CDISCTest {
    private static Logger _logger = Logger.getLogger(CDISCTest.class);

    public static void main(String[] args) {
        try {
            args = SetupEnv.getInstance().parse(args);
            CDISCTest test = new CDISCTest();
            test.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() throws Exception {
        String codingScheme = "NCI Thesaurus";
        String version = "09.12d";
        String code = "C17998";
        String associatedCode = "C66731";
        String value =
            SpecialCases.CDISC.getPreferredTerm(codingScheme, version, code,
                associatedCode);
        _logger.debug("Value: " + value);
    }
}
