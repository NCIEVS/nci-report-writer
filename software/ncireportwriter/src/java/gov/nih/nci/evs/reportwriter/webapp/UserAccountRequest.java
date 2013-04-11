/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.utils.*;
import gov.nih.nci.security.authentication.*;

import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (Will Garcia, David Yee)
 * @version 1.0
 */

public class UserAccountRequest {
    private static Logger _logger = Logger.getLogger(UserAccountRequest.class);

    public static final String LOGIN_ID = "loginID";

    public String clear() {
        HttpServletRequest request = HTTPUtils.getRequest();
        request.removeAttribute(LOGIN_ID);
        return "clear";
    }

    public String unlock() {
        _logger.debug("Method: UserAccountRequest.unlock");
        HttpServletRequest request = HTTPUtils.getRequest();
        String userid = HTTPUtils.getParameter(request, LOGIN_ID);

        if (userid == null || userid.trim().length() <= 0)
            return HTTPUtils.warningMsg(request,
                "Please enter a valid Login ID.");

        LockoutManager mgr = LockoutManager.getInstance();
        if (!mgr.isUserLockedOut(userid))
            return HTTPUtils.warningMsg(request,
                "User's login ID is either unknown or not locked.");

        _logger.debug("Unlocking userid: " + userid);
        mgr.unLockUser(userid);
        HTTPUtils.infoMsg(request, "User (" + userid + ") has been unlocked.");
        clear();
        return "unlock";
    }
}
