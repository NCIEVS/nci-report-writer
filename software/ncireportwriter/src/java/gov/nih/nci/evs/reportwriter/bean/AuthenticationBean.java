package gov.nih.nci.evs.reportwriter.bean;

import java.io.*;
import java.util.*;
import java.net.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationBean
{
	static String ADMIN_ROLE = "admin";
	static Factory<SecurityManager> factory = null;
	static SecurityManager securityManager = null;

    private final static String SHIRO_INI_FILE_ENV =
        "gov.nih.nci.cacore.ncireportwritershiroini";

// Variable declaration
	private String _userid;
	private String _password;
	private long _roleGroupId;
	private String _selectedTask;
	private Boolean _isAdmin;
	//private InitialContext _context;


// Default constructor
	public AuthenticationBean() {
	}

// Constructor
	public AuthenticationBean(
		String _userid,
		String _password,
		long _roleGroupId,
		String _selectedTask,
		Boolean _isAdmin) {
		//InitialContext _context) {

		this._userid = _userid;
		this._password = _password;
		this._roleGroupId = _roleGroupId;
		this._selectedTask = _selectedTask;
		this._isAdmin = _isAdmin;
		//this._context = _context;
	}

	static {
		String iniFile = System.getProperty(SHIRO_INI_FILE_ENV);
        //factory = new IniSecurityManagerFactory("shiro.ini");
        factory = new IniSecurityManagerFactory(iniFile);
        securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
	}

// Set methods
	public void set_userid(String _userid) {
		this._userid = _userid;
	}

	public void set_password(String _password) {
		this._password = _password;
	}

	public void set_roleGroupId(long _roleGroupId) {
		this._roleGroupId = _roleGroupId;
	}

	public void set_selectedTask(String _selectedTask) {
		this._selectedTask = _selectedTask;
	}

	public void set_isAdmin(Boolean _isAdmin) {
		this._isAdmin = _isAdmin;
	}

	//public void set_context(InitialContext _context) {
	//	this._context = _context;
	//}


// Get methods
	public String get_userid() {
		return this._userid;
	}

	public String get_password() {
		return this._password;
	}

	public long get_roleGroupId() {
		return this._roleGroupId;
	}

	public String get_selectedTask() {
		return this._selectedTask;
	}

	public Boolean get_isAdmin() {
		return this._isAdmin;
	}

	//public InitialContext get_context() {
	//	return this._context;
	//}

    public String loginAction() {
        Subject currentUser = SecurityUtils.getSubject();
        /*
        // Do some stuff with a Session (no need for a web or EJB container!!!)
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            System.out.println("Retrieved the correct value! [" + value + "]");
        }
        */
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(_userid, _password);
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                System.out.println("There is no user with username of " + token.getPrincipal());
                return "failure";
            } catch (IncorrectCredentialsException ice) {
                System.out.println("Password for account " + token.getPrincipal() + " was incorrect!");
                return "failure";
            } catch (LockedAccountException lae) {
                System.out.println("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
                return "failure";
            }
            catch (AuthenticationException ae) {
                return "failure";
            }
        }
        System.out.println("=========================== LOGIN successful");
        if (currentUser.hasRole("admin")) {
            System.out.println("You have admin role.");
        } else {
            System.out.println("You do not have admin role.");
        }
        return "success";
        /*

        //say who they are:
        //print their identifying principal (in this case, a username):
        System.out.println("============ User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        if (currentUser.hasRole("schwartz")) {
            System.out.println("May the Schwartz be with you!");
        } else {
            System.out.println("Hello, mere mortal.");
        }

        //test a typed permission (not instance-level)
        if (currentUser.isPermitted("lightsaber:weild")) {
            System.out.println("You may use a lightsaber ring.  Use it wisely.");
        } else {
            System.out.println("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //a (very powerful) Instance Level permission:
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            System.out.println("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            System.out.println("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out!
        //currentUser.logout();



           return "success";
        } catch (Exception e) {
            String msg = reformatError(e.getMessage());
            _logger.error(StringUtils.SEPARATOR);
            ExceptionUtils
                .print(_logger, e, "  * Error logging in: " + _userid);
            HTTPUtils.getRequest().setAttribute("loginWarning", msg);
            return "failure";
        */

	}

    public boolean hasAdminPrivilege() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser == null) return false;
		return hasAdminRole(currentUser);
	}

	public boolean hasAdminRole(Subject currentUser) {
		if (currentUser == null) return false;
		return hasRole(currentUser, ADMIN_ROLE);
	}

	public boolean hasRole(Subject currentUser, String roleName) {
		if (currentUser == null) return false;
		if (currentUser.hasRole(roleName)) {
			return true;
		}
		return false;
	}

    public boolean hasRole(String roleName) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser == null) return false;
        return hasRole(currentUser, roleName);
	}

    public boolean authenticate(String username, String password) {
        Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(true);
		try {
			currentUser.login(token);
			return true;
		} catch (UnknownAccountException uae) {
			System.out.println("There is no user with username of " + token.getPrincipal());
		} catch (IncorrectCredentialsException ice) {
			System.out.println("Password for account " + token.getPrincipal() + " was incorrect!");
		} catch (LockedAccountException lae) {
			System.out.println("The account for username " + token.getPrincipal() + " is locked.  " +
					"Please contact your administrator to unlock it.");
		}
		// ... catch more exceptions here (maybe custom ones specific to your application?
		catch (AuthenticationException ae) {
			//unexpected condition?  error?
		}
        return false;
    }

    public static void main(String[] args) {
		String username = (String) args[0];
		String password = (String) args[1];
		AuthenticationBean bean = new AuthenticationBean();

		boolean authenticated = bean.authenticate(username, password);
		System.out.println("---- Is user " + username + " authenticated? " + authenticated);
		System.out.println("---- Does user " + username + " hasAdminPrivilege? " + bean.hasAdminPrivilege());

		username = "guest";
		password = "guest";
		authenticated = new AuthenticationBean().authenticate(username, password);
		System.out.println("---- Is user " + username + " authenticated? " + authenticated);
		System.out.println("---- Does user " + username + " hasAdminPrivilege? " + bean.hasAdminPrivilege());

		username = "anyotheruser";
		password = "anyotheruser";
		authenticated = new AuthenticationBean().authenticate(username, password);
		System.out.println("---- Is user " + username + " authenticated? " + authenticated);
	}
}
