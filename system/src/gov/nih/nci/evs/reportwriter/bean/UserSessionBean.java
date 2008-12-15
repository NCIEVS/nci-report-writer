
package gov.nih.nci.evs.reportwriter.bean;

import java.util.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.AuthenticationManager;

import gov.nih.nci.evs.reportwriter.utils.*;


import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

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
	  String userid;
	  String password;

	  int roleGroupId;
	  String selectedTask = null;

	  public String getSelectedTask()
	  {
		  return this.selectedTask;
	  }

	  public void setSelectedTask(String selectedTask)
	  {
		  this.selectedTask = selectedTask;;
	  }


	  public void setRoleGroupId(int roleId) {
		  this.roleGroupId = roleId;
	  }
/**
	  public int getRoleGroupIdFromCSM(String userId) {
		  user = ami.getUser(userId);
		  System.out.println("User ID: " + user.getUserId() + " User paswd: " + user.getPassword());
		  Set pgroles = user.getProtectionGroupRoleContexts();
		  System.out.println("The user: " + userId + " has been assigned " + pgroles.size() + "protection groups");
		   
		  while(pgroles.iterator().hasNext()) {
		        pgrc = (ProtectionGroupRoleContext) pgroles.iterator().next();
		        Set roles = pgrc.getRoles();
		        while(roles.iterator().hasNext()) {
		              ur = (UserRole) roles.iterator().next();
		              int roleid = ur.getRoleID();
		        }
		  }
	  }
	*/  
	  public List getTaskList()
	  {
		  // To be modified:
		  //
		  // (1) Find user role from csmupt database through CSM API.
		  // (2) call setRoleGroupId to set roleGroupId
		  // (3) replace the following statement by:
		  //     return DataUtils.getTaskList(roleGroupId);

		  //setRoleGroupId(getRoleGroupIdFromCSM(userId));
		  //return DataUtils.getTaskList(roleGroupId);
		  return DataUtils.getTaskList(1); // temporarily set role to 1 (admin)
	  }

	  public void changeTaskSelection(ValueChangeEvent vce) {
		 String newValue = (String)vce.getNewValue();
		 selectedTask = newValue;
	  }

  }