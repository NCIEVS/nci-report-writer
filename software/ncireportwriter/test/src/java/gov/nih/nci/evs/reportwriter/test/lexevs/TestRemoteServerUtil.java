package gov.nih.nci.evs.reportwriter.test.lexevs;

import org.LexGrid.LexBIG.LexBIGService.*;
import org.apache.log4j.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import junit.framework.*;

public class TestRemoteServerUtil extends TestCase {
    private static Logger _logger = Logger.getLogger(TestRemoteServerUtil.class);
    
    public static void main(String[] args) {
        TestRemoteServerUtil test = new TestRemoteServerUtil();
        test.test_createLexBIGService();
    }

    public void test_createLexBIGService() {
        try {
            TestEnv.setup();
            
            LexBIGService server =
                RemoteServerUtil.createLexBIGService();
            _logger.info("server: " + server);
            assertTrue(server != null);
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + 
                ": " + e.getLocalizedMessage());
        }
    }
}
