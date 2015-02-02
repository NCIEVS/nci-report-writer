/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

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

import java.net.*;
import java.sql.*;
import javax.sql.DataSource;

import gov.nih.nci.security.util.*;
import gov.nih.nci.evs.reportwriter.security.CSMAuthorizationManager;

/**
 *
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class LoginBean extends Object {
	private static Logger _logger = Logger.getLogger(LoginBean.class);
	private static final String ADDRESS = "2115 East Jefferson, Rockville 20852";
    private static final String APP_NAME = "ncireportwriter";
    private static final String CSM_LOCKOUT_TIME =
        AppProperties.getInstance()
            .getProperty(AppProperties.CSM_LOCKOUT_TIME);
    private static final String CSM_ALLOWED_LOGIN_TIME =
        AppProperties.getInstance()
            .getProperty(AppProperties.CSM_ALLOWED_LOGIN_TIME);
    private static final String CSM_ALLOWED_ATTEMPTS = "1000";
        //AppProperties.getInstance()
        //    .getProperty(AppProperties.CSM_ALLOWED_ATTEMPTS);

    private String _userid;
    private String _password;
    private long _roleGroupId;
    private String _selectedTask = null;
    private Boolean _isAdmin = null;
    private InitialContext _context = null;

    public LoginBean() {
		super();
		LockoutManager.initialize(CSM_LOCKOUT_TIME, CSM_ALLOWED_LOGIN_TIME,
				CSM_ALLOWED_ATTEMPTS);
	}

    public void setSelectedTask(String selectedTask) {
        _selectedTask = selectedTask;
        _logger.debug("selectedTask: " + _selectedTask);
    }

    public String getUserid() {
        return _userid;
    }

    public void setUserid(String newUserid) {
        _userid = newUserid;
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



    public static gov.nih.nci.security.authorization.domainobjects.User getCSMUser(String userid) throws Exception {
        AuthorizationManager manager = CSMAuthorizationManager.getAuthorizationManagerDirectly(APP_NAME);
        if (manager == null)
            throw new Exception("Can not get authorization manager for: " + APP_NAME);

        gov.nih.nci.security.authorization.domainobjects.User user = manager.getUser(userid);
        if (user == null)
            throw new Exception("Error retrieving CSM userid " + userid + ".");

        return user;
    }


    private Boolean hasAdminPrivilege(String userid) throws Exception {
        gov.nih.nci.security.authorization.domainobjects.User user = getCSMUser(userid);

        if (user == null) {
			System.out.println("getCSMUser returns NULL??? " + userid);
		}

        //KLO
        //AuthorizationManager manager = SecurityServiceProvider.getAuthorizationManager(APP_NAME);
        AuthorizationManager manager = CSMAuthorizationManager.getAuthorizationManagerDirectly(APP_NAME);

        boolean permission =
            manager.checkPermission(user.getLoginName(), "admin-pe", "EXECUTE");

        return new Boolean(permission);


        //return new Boolean(true);
    }

    private String getEmail(String userid) throws Exception {
        gov.nih.nci.security.authorization.domainobjects.User user = getCSMUser(userid);
        String email = user.getEmailId();
        return email != null ? email : null;
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

/*
String url = "jdbc:mysql://localhost/test";
Class.forName ("com.mysql.jdbc.Driver").newInstance ();
Connection conn = DriverManager.getConnection (url, "username", "password");


    <Resource name="ncirw"
              auth="Container"
              type="javax.sql.DataSource"
              username="@database.user@"
              password="@database.password@"
              driverClassName="@database.driver@"
              url="@database.url@"
              maxActive="10"
              maxIdle="4"/>


*/

	public boolean validateUser(String user, String password) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean match = false;
		try {
			/*
			String url = "jdbc:mysql://localhost/ncirw";
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			Connection conn = DriverManager.getConnection (url, "username", "password");
			*/

			InitialContext ctx = new InitialContext();
			//DataSource ds = (DataSource) ctx.lookup("ncirw");

			// This works too
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup(APP_NAME);

			conn = ds.getConnection();
			try {
				password = new StringEncrypter().encrypt(password);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			st = conn.createStatement();
			rs = st.executeQuery("SELECT LOGIN_NAME, PASSWORD FROM csm_user where LOGIN_NAME = " + "\"" + user + "\"" + " and PASSWORD = "
			     + "\"" + password + "\"");

			while (rs.next()) {
				match = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (st != null) st.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return match;
	}




    public String loginAction() {


_logger.debug("(*****************) calling loginAction ... " );

        //useDebugUserid();


        try {
            _isAdmin = false;
            if (_userid.length() <= 0)
                throw new Exception("Please enter your login ID.");
            if (_password.length() <= 0)
                throw new Exception("Please enter your password.");

_logger.debug("_userid: " + _userid);
_logger.debug("_password: " + _password);
_logger.debug("SecurityServiceProvider.getAuthenticationManager: APP_NAME " +  APP_NAME);
_logger.debug("SecurityServiceProvider.getAuthenticationManager: CSM_LOCKOUT_TIME " +  CSM_LOCKOUT_TIME);
_logger.debug("SecurityServiceProvider.getAuthenticationManager: CSM_ALLOWED_LOGIN_TIME " +  CSM_ALLOWED_LOGIN_TIME);
_logger.debug("SecurityServiceProvider.getAuthenticationManager: CSM_ALLOWED_ATTEMPTS " +  CSM_ALLOWED_ATTEMPTS);


            AuthenticationManager authenticationManager =
                SecurityServiceProvider.getAuthenticationManager(APP_NAME,
                    CSM_LOCKOUT_TIME, CSM_ALLOWED_LOGIN_TIME,
                    CSM_ALLOWED_ATTEMPTS);


if (authenticationManager == null) {
   throw new Exception("NULL authenticationManager???");
}

            if (!authenticationManager.login(_userid, _password)) {
            //if (!validateUser(_userid, _password)) {
                throw new Exception("Incorrect login credential.");
			}

_logger.debug("(*) SecurityServiceProvider.login: success --  continue..." +  _userid);


            HttpServletRequest request = HTTPUtils.getRequest();
            HttpSession session = request.getSession(); // true
            if (session != null)
                session.setAttribute("uid", _userid);
_logger.debug("(************) calling .hasAdminPrivilege.." );

            _isAdmin = hasAdminPrivilege(_userid);
_logger.debug("(*) hasAdminPrivilege? " + _isAdmin);


            session.setAttribute("isAdmin", _isAdmin);
            String email = getEmail(_userid);
            session.setAttribute("email", email);
            gov.nih.nci.evs.reportwriter.bean.User user = getUser(_userid);
            if (user == null) {
                // Synchronize with CSM User table
                SDKClientUtil sdkclientutil = new SDKClientUtil();
                sdkclientutil.insertUser(_userid);
            }

            session.setAttribute("isSessionValid", Boolean.TRUE);
            HTTPUtils.getRequest().removeAttribute("loginWarning");
            return "success";
        } catch (Exception e) {
            String msg = reformatError(e.getMessage());
            _logger.error(StringUtils.SEPARATOR);
            ExceptionUtils
                .print(_logger, e, "  * Error logging in: " + _userid);
            HTTPUtils.getRequest().setAttribute("loginWarning", msg);
            return "failure";
        }
    }

    private void useDebugUserid() {
        if (!AppProperties.getInstance().getBoolProperty(
            AppProperties.DEBUG_ON, false))
            return;
        if (_userid.length() <= 0)
            _userid = "rwadmin";
        if (_password.length() <= 0)
            _password = modify(ADDRESS);
    }

    private String modify(String text) {
        text = new StringBuffer(text).reverse().toString();
        text = text.replace("tsaE ", "");
        text = text.replace("ellivkcoR ", "");
        text = text.replaceAll(",", "");
        text = text.replaceAll(" ", ".");
        return text;
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