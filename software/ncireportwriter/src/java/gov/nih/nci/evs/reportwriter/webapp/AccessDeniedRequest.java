/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class AccessDeniedRequest {
    // -------------------------------------------------------------------------
    private static Logger _logger = Logger.getLogger(AccessDeniedRequest.class);
    private final static String MAIL_SERVER =
        AppProperties.getInstance().getProperty(AppProperties.MAIL_SMTP_SERVER);
    private final static String ACCOUNT_ADMIN_USER_EMAIL =
        AppProperties.getInstance().getProperty(
            AppProperties.ACCOUNT_ADMIN_USER_EMAIL);

    // -------------------------------------------------------------------------
    public final static String PROBLEM = "problem";
    public final static String LOGIN_ID = "loginID";
    public final static String EMAIL = "email";
    public final static String INFORMATION = "information";
    private boolean _sendEmail = true;

    // -------------------------------------------------------------------------
    private class AccessDeniedInfo {
        public String problem = "";
        public String loginID = "";
        public String email = "";
        public String information = "";

        private void init() {
            problem = loginID = email = information = "";
        }

        public void getValues(HttpServletRequest request) {
            problem = HTTPUtils.getParameter(request, PROBLEM);
            loginID = HTTPUtils.getParameter(request, LOGIN_ID);
            email = HTTPUtils.getParameter(request, EMAIL);
            information = HTTPUtils.getParameter(request, INFORMATION);
        }

        public void clearValues(HttpServletRequest request) {
            init();
            request.removeAttribute(PROBLEM);
            request.removeAttribute(LOGIN_ID);
            request.removeAttribute(EMAIL);
            request.removeAttribute(INFORMATION);
        }

        public void debug() {
            if (!_logger.isDebugEnabled())
                return;

            _logger.debug("");
            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("AccessDeniedInfo:");
            _logger.debug("  * problem: " + problem);
            _logger.debug("  * loginID: " + loginID);
            _logger.debug("  * email: " + email);
            _logger.debug("  * information: " + information);
        }
    }

    public String clear() {
        HttpServletRequest request = HTTPUtils.getRequest();
        StringBuffer warningMsg = new StringBuffer();
        try {
            AccessDeniedInfo info = new AccessDeniedInfo();
            info.clearValues(request);
            return "clear";
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
            return HTTPUtils.warningMsg(request, warningMsg, e);
        }
    }

    public String submit() {
        HttpServletRequest request = HTTPUtils.getRequest();
        StringBuffer warningMsg = new StringBuffer();
        try {
            AccessDeniedInfo info = new AccessDeniedInfo();
            info.getValues(request);

            if (!isValidValues(warningMsg, info))
                return HTTPUtils.warningMsg(request, warningMsg);
            email(info);
            HTTPUtils.infoMsg(request, "Your problem as been reported.");
            return "submit";
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
            return HTTPUtils.warningMsg(request, warningMsg, e);
        }
    }

    private boolean isValidValues(StringBuffer warningMsg, AccessDeniedInfo info) {
        info.debug();

        if (info.problem == null || info.problem.length() <= 0)
            warningMsg.append("\n    * Your problem");
        if (info.loginID == null || info.loginID.length() <= 0)
            warningMsg.append("\n    * Login ID");
        if (info.email == null || info.email.length() <= 0)
            warningMsg.append("\n    * Email");
        if (warningMsg.length() > 0) {
            warningMsg.insert(0, "Please enter the following value(s):");
            return false;
        }

        if (!MailUtils.isValidEmailAddress(info.email))
            warningMsg.append("\n    * Email");
        if (warningMsg.length() > 0) {
            warningMsg.insert(0, "The following value(s) are invalid:");
            return false;
        }
        return true;
    }

    private void email(AccessDeniedInfo info) throws Exception {
        String from = info.email;
        String recipients = ACCOUNT_ADMIN_USER_EMAIL;
        String subject =
            "NCI Report Writer account access problems (for " + info.loginID
                + ") ...";
        StringBuffer message = new StringBuffer();
        message.append("* Problem: " + info.problem + "\n");
        message.append("* Login ID: " + info.loginID + "\n");
        message.append("* Email: " + info.email + "\n");
        message.append("* Additional Information: " + info.information + "\n");
        MailUtils.postMail(MAIL_SERVER, from, recipients, subject, message
            .toString(), _sendEmail);
    }
}
