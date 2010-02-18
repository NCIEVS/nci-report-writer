package gov.nih.nci.evs.reportwriter.test.lexevs;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.test.utils.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import org.apache.log4j.*;

public class FindRelatedCodesTest {
    private static Logger _logger = Logger.getLogger(FindRelatedCodesTest.class);

    public static void main(String[] args) {
        args = SetupEnv.getInstance().parse(args);
        FindRelatedCodesTest test = new FindRelatedCodesTest();
        test.test1();
    }
    
    public void test1() {
        ReportWriterProperties.getInstance();  // Not needed.
        try {
            FindRelatedCodesUtil util = new FindRelatedCodesUtil();
            String scheme = "NCI Thesaurus";
            String version = null;
            String code = "C63923";
            code = "C50372";
            String assoName = "A10";
            boolean direction = true;
            int maxReturn = 100;
            
            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("Calling Method: FindRelatedCodesUtil.getAssociatedConcepts");
            _logger.debug("  * scheme: " + scheme);
            _logger.debug("  * version: " + version);
            _logger.debug("  * code: " + code);
            _logger.debug("  * assoName: " + assoName);
            _logger.debug("  * direction: " + direction);
            _logger.debug("  * maxReturn: " + maxReturn);
            util.getAssociatedConcepts(scheme, version, code, assoName,
                direction, maxReturn);
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
        }
    }
}
