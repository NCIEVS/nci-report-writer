package gov.nih.nci.evs.reportwriter.test.utils;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;

import org.apache.log4j.*;

public class MailUtilsTest {
    private static Logger _logger = Logger.getLogger(MailUtilsTest.class);

    public static void main(String[] args) {
        args = SetupEnv.getInstance().parse(args);
        new MailUtilsTest().test();
    }
    
    public void test1() {
        String value = "   yeed@mail.nih.gov   ;     ;;;a;b;adf; afdfsadf ; dfadf ";
        String values[] = StringUtils.toStrings(value, ";", false);
        _logger.debug("List of values:");
        for (int i=0; i<values.length; ++i)
            _logger.debug("  " + i + ") " + values[i]);
    }

    public void test() {
        try {
            String mailServer =
                AppProperties.getInstance().getProperty(
                    AppProperties.MAIL_SMTP_SERVER);
            String sender = "a@b.c; b@b.c";
            String recipients = "yeed@mail.nih.gov";
            String subject = "Testing MailUtils";
            String message = "Testing\n1\n2\n3";
            boolean send = true;
            MailUtils.postMail(mailServer, sender, recipients, subject, message, send);
            _logger.debug("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


