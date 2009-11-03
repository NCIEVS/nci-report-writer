package gov.nih.nci.evs.reportwriter.test.lexevs;

import org.LexGrid.LexBIG.LexBIGService.*;
import org.apache.log4j.*;

import gov.nih.nci.evs.reportwriter.test.utils.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import junit.framework.*;

public class RemoteServerUtilTest extends TestCase {
    private static Logger _logger = Logger.getLogger(RemoteServerUtilTest.class);
    
    public static void main(String[] args) {
        args = TestEnv.parse(args);
        RemoteServerUtilTest test = new RemoteServerUtilTest();
        test.test_createLexBIGService();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        TestEnv.setup();
    }
    
    public void test_createLexBIGService() {
        try {
            LexBIGService server =
                RemoteServerUtil.createLexBIGService();
            _logger.info("server: " + server);
            assertTrue(server != null);
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + 
                ": " + e.getLocalizedMessage());
            assertTrue(false);
        }
    }
}