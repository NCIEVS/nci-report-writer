
package gov.nih.nci.evs.reportwriter.bean;

import gov.nih.nci.evs.reportwriter.utils.DataUtils;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2007 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

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
		  // To be modified:
		  //
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

			/*
		  long rid = -1;
		  boolean isAdmin = false;
		  AuthorizationManager ami = null;

		  try {
			  System.out.println("In userSessionBean getTaskList " + this.roleGroupId);
			  ami = SecurityServiceProvider.getAuthorizationManager("ncireportwriter");
			  if (ami == null) {
					throw new Exception();
			  }
			  ami = (AuthorizationManager) ami;

			  User user = ami.getUser(userid);
			  System.out.println("UserSessionBean: User ID: " + user.getUserId() + " User paswd: " + user.getPassword());

			  //Set<Group> groups = user.getGroups();
			  Set<Group> groups = ami.getGroups(user.getUserId().toString());

			  if(null != groups) {
				  System.out.println("Groups set is not null. User is part of : " + groups.size() + " groups");

				  Iterator iter = groups.iterator();
				  while(iter.hasNext()) {
					    Group grp = (Group) iter.next();
					    if(null != grp) {
					    	System.out.println("GROUP is NOT null");
					    	rid = grp.getGroupId();
					    	if(grp.getGroupName().startsWith("admin")) {
					    		System.out.println("User associated with an admin group");
					    		isAdmin = true;
					    		break;
					    	}
					    }
				  }
			  }
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }

		  setRoleGroupId(rid);
		  System.out.println("In UserSessionBean.getTaskList() roleGroupId here is: " + this.roleGroupId);
		  KLO_log.warn("UserSessionBean: isAdmin: " + isAdmin);
		  */

		  return DataUtils.getTaskList(isAdmin); // temporarily set role to 1 (admin)
	  }

	  public void changeTaskSelection(ValueChangeEvent vce) {
		 String newValue = (String)vce.getNewValue();
		 selectedTask = newValue;
	  }

  }