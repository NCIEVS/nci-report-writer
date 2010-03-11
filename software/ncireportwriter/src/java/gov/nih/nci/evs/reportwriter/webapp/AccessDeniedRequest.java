package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class AccessDeniedRequest {
    // -------------------------------------------------------------------------
    private static Logger _logger = Logger.getLogger(AccessDeniedRequest.class);
    private final static String MAIL_SERVER =
        AppProperties.getInstance()
            .getProperty(AppProperties.MAIL_SMTP_SERVER);
    private final static String ACCOUNT_ADMIN_USER_EMAIL =
        AppProperties.getInstance()
            .getProperty(AppProperties.ACCOUNT_ADMIN_USER_EMAIL);

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
        HttpServletRequest request = SessionUtil.getRequest();
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
        HttpServletRequest request = SessionUtil.getRequest();
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

    private boolean isValidValues(StringBuffer warningMsg,
        AccessDeniedInfo info) {
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
        String subject = "NCI Report Writer account access problems (for " 
            + info.loginID + ") ...";
        StringBuffer message = new StringBuffer();
        message.append("* Problem: " + info.problem + "\n");
        message.append("* Login ID: " + info.loginID + "\n");
        message.append("* Email: " + info.email + "\n");
        message.append("* Additional Information: " + info.information + "\n");
        MailUtils.postMail(MAIL_SERVER, from, recipients, subject, message
            .toString(), _sendEmail);
    }
}
