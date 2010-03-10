package gov.nih.nci.evs.reportwriter.test.utils;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import org.apache.log4j.*;

public class MailUtilsTest {
    private static Logger _logger = Logger.getLogger(MailUtilsTest.class);

    public static void main(String[] args) {
        args = SetupEnv.getInstance().parse(args);
        new MailUtilsTest().test();
    }

    public void test() {
        try {
            String mailServer =
                AppProperties.getInstance().getProperty(
                    AppProperties.MAIL_SMTP_SERVER);
            String from = "yeed@mail.nih.gov";
            String[] recipients = new String[] { from };
            String subject = "Testing MailUtils";
            String message = "Testing\n1\n2\n3";
            boolean send = true;
            MailUtils.postMail(mailServer, from, recipients, subject, message
                .toString(), send);
            _logger.debug("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


