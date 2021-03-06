/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.webapp;

import javax.servlet.http.*;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class ContactUsRequest {
    // -------------------------------------------------------------------------
    public static final String SUBJECT = "subject";
    public static final String EMAIL_MSG = "email_msg";
    public static final String EMAIL_ADDRESS = "email_address";
    private boolean _isSendEmail = true;

    // -------------------------------------------------------------------------
    public String clear() {
        HttpServletRequest request = HTTPUtils.getRequest();
        request.removeAttribute(SUBJECT);
        request.removeAttribute(EMAIL_MSG);
        request.removeAttribute(EMAIL_ADDRESS);
        return "clear";
    }

    public String submit() {
        HttpServletRequest request = HTTPUtils.getRequest();
        AppProperties appProperties = AppProperties.getInstance();
        try {
            String mailServer =
                appProperties.getProperty(AppProperties.MAIL_SMTP_SERVER);
            String subject = HTTPUtils.getParameter(request, SUBJECT);
            String emailMsg = HTTPUtils.getParameter(request, EMAIL_MSG);
            String from = HTTPUtils.getParameter(request, EMAIL_ADDRESS);
            String recipients =
                appProperties.getProperty(AppProperties.CONTACT_US_EMAIL);
            MailUtils.postMail(mailServer, from, recipients, subject, emailMsg,
                _isSendEmail);
        } catch (UserInputException e) {
            return HTTPUtils.warningMsg(request, e.getMessage());
        } catch (Exception e) {
            StringBuffer warningMsg = new StringBuffer();
            warningMsg.append("System Error: Your message was not sent.\n");
            warningMsg
                .append("    (If possible, please contact NCI systems team.)\n");
            warningMsg.append("\n");
            warningMsg.append(e.getMessage() + "\n");
            return HTTPUtils.warningMsg(request, warningMsg);
        }

        HTTPUtils.infoMsg(request, "Your message was successfully sent.");
        clear();
        return "submit";
    }
}
