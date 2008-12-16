
package gov.nih.nci.evs.reportwriter.bean;

import gov.nih.nci.evs.reportwriter.utils.DataUtils;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
  * @author EVS Team
  * @version 1.0
 */

public class UserSessionBean extends Object
{
	  private static Logger KLO_log = Logger.getLogger("LoginBean KLO");

	  String userid;
	  String password;

	  Boolean isAdmin = null;


	  long roleGroupId;
	  String selectedTask = null;

      public void setIsAdmin(Boolean bool_obj)
      {
		  this.isAdmin = bool_obj;
	  }

      public Boolean getIsAdmin()
      {
		  return this.isAdmin;
	  }

	  public String getSelectedTask()
	  {
		  return this.selectedTask;
	  }

	  public void setUserId(String userid)
	  {
		  this.userid = userid;
	  }

	  public void setSelectedTask(String selectedTask)
	  {
		  this.selectedTask = selectedTask;;
	  }

	  public void setRoleGroupId(long roleId) {
		  this.roleGroupId = roleId;
	  }

	  public List getTaskList()
	  {
		  // (1) Find user role from csmupt database through CSM API.
		  // (2) call setRoleGroupId to set roleGroupId
		  // (3) replace the following statement by:
		  //     return DataUtils.getTaskList(roleGroupId);

		    FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession session = request.getSession(false);
			Boolean isAdmin = null;
			if (session != null) {
				 isAdmin = (Boolean) request.getSession(true).getAttribute("isAdmin");
			}

		  return DataUtils.getTaskList(isAdmin); // temporarily set role to 1 (admin)
	  }

	  public void changeTaskSelection(ValueChangeEvent vce) {
		 String newValue = (String)vce.getNewValue();
		 selectedTask = newValue;
	  }

  }