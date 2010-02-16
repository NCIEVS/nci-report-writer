package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.utils.*;

import javax.servlet.http.*;

import org.apache.log4j.*;

public class AccountRequest {
    private static Logger _logger = Logger.getLogger(AccountRequest.class);
    public final static String PROBLEM = "problem";
    public final static String LOGIN_ID = "loginID";
    public final static String EMAIL = "email";
    public final static String INFORMATION = "information";

    private static class AccessDeniedInfo {
        public String problem = "";
        public String loginID = "";
        public String email = "";
        public String information = "";
        
        public AccessDeniedInfo(HttpServletRequest request) {
            problem = HTTPUtils.getParameter(request, PROBLEM);
            loginID = HTTPUtils.getParameter(request, LOGIN_ID);
            email = HTTPUtils.getParameter(request, EMAIL);
            information = HTTPUtils.getParameter(request, INFORMATION);
        }
    }
    
    public static String submitAccessDenied() {
        HttpServletRequest request = SessionUtil.getRequest();
        StringBuffer warningMsg = new StringBuffer();
        try {
            AccessDeniedInfo info = new AccessDeniedInfo(request);

            if (!isValidAccessDenied(warningMsg, info))
                return HTTPUtils.warningMsg(request, warningMsg);

            return "request";
        } catch (Exception e) {
            e.printStackTrace();
            return HTTPUtils.warningMsg(request, warningMsg, e);
        }
    }

    private static boolean isValidAccessDenied(StringBuffer warningMsg,
        AccessDeniedInfo info) {
        _logger.debug("");
        _logger.debug(StringUtils.SEPARATOR);

        _logger.debug("  * problem: " + info.problem);
        if (info.problem == null || info.problem.length() <= 0)
            warningMsg.append("\n    * Your problem");

        _logger.debug("  * loginID: " + info.loginID);
        if (info.loginID == null || info.loginID.length() <= 0)
            warningMsg.append("\n    * Login ID");

        _logger.debug("  * email: " + info.email);
        if (info.email == null || info.email.length() <= 0)
            warningMsg.append("\n    * Email");

        _logger.debug("  * information: " + info.information);
        
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
}
