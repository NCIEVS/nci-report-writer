/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test.lexevs;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.apache.log4j.*;

import gov.nih.nci.evs.reportwriter.test.utils.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;
import junit.framework.*;

public class RemoteServerUtilTest extends TestCase {
    private static Logger _logger = Logger.getLogger(RemoteServerUtilTest.class);
    
    public static void main(String[] args) {
        args = SetupEnv.getInstance().parse(args);
        RemoteServerUtilTest test = new RemoteServerUtilTest();
        test.test_createLexBIGService();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        SetupEnv.getInstance();
    }
    
    public void test_createLexBIGService() {
        try {
            LexBIGService server =
                RemoteServerUtil.createLexBIGService();
            simpleTest(server);
            assertTrue(server != null);
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
            assertTrue(false);
        }
    }
    
    private static void simpleTest(LexBIGService lbSvc) throws Exception {
        CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
        CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
        _logger.debug("List of coding schemes:");
        for (int i = 0; i < csrs.length; i++) {
            CodingSchemeRendering csr = csrs[i];
            CodingSchemeSummary css = csr.getCodingSchemeSummary();
            String name = css.getFormalName();
            String version = css.getRepresentsVersion();
            _logger.debug("  " + i + ") " + name + "(version: " + version + ")");
        }
    }
}
