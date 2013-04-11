package gov.nih.nci.evs.reportwriter.bean;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;
import gov.nih.nci.security.*;
import gov.nih.nci.security.authentication.*;
import gov.nih.nci.security.authorization.domainobjects.User;

import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
import javax.naming.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class LoginBean extends Object {
	
	private static Logger _logger = Logger.getLogger(LoginBean.class);
    private static final String APP_NAME = "ncireportwriter";
    private static final String CSM_LOCKOUT_TIME =
        AppProperties.getInstance()
            .getProperty(AppProperties.CSM_LOCKOUT_TIME);
    private static final String CSM_ALLOWED_LOGIN_TIME =
        AppProperties.getInstance()
            .getProperty(AppProperties.CSM_ALLOWED_LOGIN_TIME);
    private static final String CSM_ALLOWED_ATTEMPTS =
        AppProperties.getInstance()
            .getProperty(AppProperties.CSM_ALLOWED_ATTEMPTS);

    private String _userid = "rwadmin";
    private String _password;
    private long _roleGroupId;
    private String _selectedTask = null;
    private Boolean _isAdmin = null;
    private InitialContext _context = null;

    public LoginBean() {
		super();
//		LockoutManager.initialize(CSM_LOCKOUT_TIME, CSM_ALLOWED_LOGIN_TIME,
//				CSM_ALLOWED_ATTEMPTS);
	}    
    
    public void setSelectedTask(String selectedTask) {
        _selectedTask = selectedTask;
        _logger.debug("selectedTask: " + _selectedTask);
    }

    public String getUserid() {
        return _userid;
    }

    public void setUserid(String newUserid) {
//        _userid = newUserid;
        _userid = "rwadmin";
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String newPassword) {
        _password = newPassword;
    }

    public long getRoleGroupId() {
        return _roleGroupId;
    }

    public void setRoleGroupId(long roleGroupId) {
        _roleGroupId = roleGroupId;
    }

    private User getCSMUser(String userid) throws Exception {
//        AuthorizationManager manager =
//            SecurityServiceProvider.getAuthorizationManager(APP_NAME);
//        if (manager == null)
//            throw new Exception("Can not get authorization manager for: "
//                + APP_NAME);
//
//        User user = manager.getUser(userid);
//        if (user == null)
//            throw new Exception("Error retrieving CSM userid " + userid + ".");
//        return user;
        return null;
    }

    private Boolean hasAdminPrivilege(String userid) throws Exception {
//        User user = getCSMUser(userid);
//        AuthorizationManager manager =
//            SecurityServiceProvider.getAuthorizationManager(APP_NAME);
//        boolean permission =
//            manager.checkPermission(user.getLoginName(), "admin-pe", "EXECUTE");
//        return new Boolean(permission);
        return true;
    }

    private String getEmail(String userid) throws Exception {
//        User user = getCSMUser(userid);
//        String email = user.getEmailId();
//        return email != null ? email : null;
        return "yeed@mail.nih.gov";
    }

    public List<SelectItem> getTaskList() {
        return DataUtils.getTaskList(_isAdmin);
    }

    public gov.nih.nci.evs.reportwriter.bean.User getUser(String loginName) {
        try {
            SDKClientUtil sdkClientUtil = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.User";
            String methodName = "setLoginName";
            Object obj = sdkClientUtil.search(FQName, methodName, loginName);
            if (obj == null)
                throw new Exception("Error retrieving user: " + loginName
                    + ".  sdkClientUtil.search returns null");
            gov.nih.nci.evs.reportwriter.bean.User user =
                (gov.nih.nci.evs.reportwriter.bean.User) obj;
            return user;
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e, "  * getUser(" + loginName
                + ") method returns null");
            // e.printStackTrace();
        }
        return null;
    }

    public String loginAction() {
//        useDebugUserid();
//        try {
//            _isAdmin = false;
//            if (_userid.length() <= 0)
//                throw new Exception("Please enter your login ID.");
//            if (_password.length() <= 0)
//                throw new Exception("Please enter your password.");
//           
//            AuthenticationManager authenticationManager =
//                SecurityServiceProvider.getAuthenticationManager(APP_NAME,
//                    CSM_LOCKOUT_TIME, CSM_ALLOWED_LOGIN_TIME,
//                    CSM_ALLOWED_ATTEMPTS);
//
//            if (!authenticationManager.login(_userid, _password))
//                throw new Exception("Incorrect login credential.");
//
//            HttpServletRequest request = HTTPUtils.getRequest();
//            HttpSession session = request.getSession(); // true
//            if (session != null)
//                session.setAttribute("uid", _userid);
//
//            _isAdmin = hasAdminPrivilege(_userid);
//            session.setAttribute("isAdmin", _isAdmin);
//            String email = getEmail(_userid);
//            session.setAttribute("email", email);
//
//            gov.nih.nci.evs.reportwriter.bean.User user = getUser(_userid);
//            if (user == null) {
//                // Synchronize with CSM User table
//                SDKClientUtil sdkclientutil = new SDKClientUtil();
//                sdkclientutil.insertUser(_userid);
//            }
//            session.setAttribute("isSessionValid", Boolean.TRUE);
//            HTTPUtils.getRequest().removeAttribute("loginWarning");
//            return "success";
//        } catch (Exception e) {
//            String msg = reformatError(e.getMessage());
//            _logger.error(StringUtils.SEPARATOR);
//            ExceptionUtils
//                .print(_logger, e, "  * Error logging in: " + _userid);
//            HTTPUtils.getRequest().setAttribute("loginWarning", msg);
//            return "failure";
//        }
        
        HttpServletRequest request = HTTPUtils.getRequest();
        HttpSession session = request.getSession(); // true
        session.setAttribute("isAdmin", true);
        return "success";
    }

    private void useDebugUserid() {
        if (!AppProperties.getInstance().getBoolProperty(
            AppProperties.DEBUG_ON, false))
            return;
        if (_userid.length() <= 0)
            _userid = "rwadmin";
        if (_password.length() <= 0)
            _password = "x";
    }

    private String reformatError(String text) {
        if (text.equals("Invalid Login Credentials"))
            return "Invalid login credentials.";
        if (text.equals("Allowed Attempts Reached ! User Name is locked out !"))
            return "Allowed attempts reached.  Login ID is currently locked out.";
        return text;
    }

    public void changeTaskSelection(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedTask(newValue);
    }

    public Object getService(String serviceBeanName)
            throws javax.naming.NamingException {
        return _context.lookup(serviceBeanName);
    }

    // FYI: Does not seem to be used.
    // public String logoutAction() {
    // return logout();
    // }

    // FYI: Does not seem to be used.
    // public String logout() {
    // HttpSession session = SessionUtil.getSession();
    // if (session != null)
    // session.invalidate();
    // return "logout";
    // }
}