
package gov.nih.nci.evs.reportwriter.bean;

import java.io.File;


import gov.nih.nci.evs.reportwriter.utils.DataUtils;
import gov.nih.nci.evs.reportwriter.utils.SDKClientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import java.util.Collection;



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
	  private static Logger KLO_log = Logger.getLogger("UserSessionBean KLO");

      // to be modified:
      static String download_dir = "c://ncireportwriter_download";

	  Boolean isAdmin = null;
	  String selectedTask = null;

	  private List standardReportTemplateList = new ArrayList();
	  private String selectedStandardReportTemplate = null;
	  private String selectedPropertyType = null;
	  private List propertyTypeList = new ArrayList();

	  private String rootConceptCode = null;
	  private String selectedOntology = null;

	  private String selectedReportStatus = null;
      private List reportStatusList = null;
	  private Vector<String> reportStatusListData = null;


	  public void setIsAdmin(Boolean bool_obj) {
		  this.isAdmin = bool_obj;
	  }

      public Boolean getIsAdmin() {
		  return this.isAdmin;
	  }

	  public String getSelectedTask() {
		  return this.selectedTask;
	  }

	  public void setSelectedTask(String selectedTask) {
		  this.selectedTask = selectedTask;
	  }

	  public String getSelectedPropertyType() {
		  return this.selectedPropertyType;
	  }

	  public void setSelectedPropertyType(String selectedPropertyType) {
		  this.selectedPropertyType = selectedPropertyType;
		  HttpServletRequest request = getHttpRequest();
		  request.getSession().setAttribute("selectedPropertyType", selectedPropertyType);
	  }


	  public void propertyTypeSelectionChanged(ValueChangeEvent event) {
		  if (event.getNewValue() == null) return;
		  String newValue = (String) event.getNewValue();
		  setSelectedPropertyType(newValue);
	  }


	  public String getSelectedOntology() {
		  return this.selectedOntology;
	  }

	  public void setSelectedOntology(String selectedOntology) {
		  this.selectedOntology = selectedOntology;
	  }

	  public HttpServletRequest getHttpRequest() {
		  FacesContext context = FacesContext.getCurrentInstance();
		  HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		  return request;
	  }

	  public List getTaskList() {
			HttpServletRequest request = getHttpRequest();
			HttpSession session = request.getSession(false);

			Boolean isAdmin = null;
			if (session != null) {
				 isAdmin = (Boolean) request.getSession(true).getAttribute("isAdmin");
			}

			List list = DataUtils.getTaskList(isAdmin);
			if (selectedTask == null) {
				SelectItem item = (SelectItem) list.get(0);
				selectedTask = item.getLabel();
			}

		  return DataUtils.getTaskList(isAdmin);
	  }


	  public List getPropertyTypeList() {
			List list = DataUtils.getPropertyTypeList();
			if (selectedPropertyType == null) {
				SelectItem item = (SelectItem) list.get(0);
				selectedPropertyType = item.getLabel();
			}
			return list;
	  }


	  public void changeTaskSelection(ValueChangeEvent vce) {
		  String newValue = (String)vce.getNewValue();
 		  //KLO_log.warn("============== changeTaskSelection " + newValue);
		  selectedTask = newValue;
	  }


	  public void reportSelectionChanged(ValueChangeEvent vce) {
		  String newValue = (String) vce.getNewValue();
		  //String oldValue = (String) vce.getOldValue();
          setSelectedStandardReportTemplate(newValue);
	  }


	  public List getStandardReportTemplateList() {

			List list = DataUtils.getStandardReportTemplateList();
			if (selectedStandardReportTemplate == null)
			{
			    if (list != null && list.size() > 0)
			    {
					//KLO
					if (getSelectedStandardReportTemplate() == null)
					{
						SelectItem item = (SelectItem) list.get(0);
						setSelectedStandardReportTemplate(item.getLabel());
				    }
			    }
			}
		    return list;
	  }


	  public String getSelectedStandardReportTemplate() {
		  return this.selectedStandardReportTemplate;
	  }


	  public void setSelectedStandardReportTemplate(String selectedStandardReportTemplate) {
		  this.selectedStandardReportTemplate = selectedStandardReportTemplate;
		  HttpServletRequest request = getHttpRequest();
		  request.getSession().setAttribute("selectedStandardReportTemplate", selectedStandardReportTemplate);
	  }

	  //taskSelectionChanged
	  public void taskSelectionChanged(ValueChangeEvent event) {
		  if (event.getNewValue() == null) return;
		  String task = (String) event.getNewValue();
		  setSelectedTask(task);
	  }


	  public String performTask() {
		  if (this.selectedTask.compareTo("Administer Standard Reports") == 0)
		  {
			  List list = getStandardReportTemplateList();
			  if (list == null || list.size() == 0)
			  {
				  return "add_standard_report_template";
			  }
			  return "administer_standard_reports";
		  }
		  else if (this.selectedTask.compareTo("Maintain Report Status") == 0)
		     return "report_status";

		  else if (this.selectedTask.compareTo("Assign Report Status") == 0)
		     return "assign_report_status";

		  else if (this.selectedTask.compareTo("Retrieve Standard Reports") == 0)
		     return "retrieve_standard_reports";

		  return null;
	  }


	  public String addColumnAction() {
		  //KLO_log.warn("******************************************* addColumnAction() ");
		  // add_standard_report_column.jsp
		  return "add_standard_report_column";
	  }



	  public String modifyColumnAction() {
          // not functional, to be modifid
          // need to track coding scheme
          // need to populate selected report_column data
		  return "add_standard_report_column";
	  }


	  public String insertbeforeColumnAction() {
          // not functional, to be modifid
          // need to track coding scheme
          // track selected column number
		  return "add_standard_report_column";
	  }


	  public String insertafterColumnAction() {
          // not functional, to be modifid
          // need to track coding scheme
          // track selected column number
		  return "add_standard_report_column";
	  }

	  public String deleteColumnAction() {
          // not functional, to be modifid
          // need to track coding scheme
          // track selected column number

          //selectedcolumn
		  HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
          String id_str = (String) request.getParameter("selectedcolumn");
          int id = Integer.parseInt(id_str);

System.out.println("deleting column with ID = " + id + " (yet to be implemented)" );

          try{
			  ReportColumn reportColumn = getReportColumn(id);
        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
  			  sdkclientutil.deleteReportColumn(reportColumn);
  			  //setSelectedStandardReportTemplate(label);

          } catch(Exception e) {
        	  e.printStackTrace();
          }

		  return "standard_report_column";
	  }


	  public String getRootConceptCode() {
		  return this.rootConceptCode;
	  }


	  public void setRootConceptCode(String rootConceptCode) {
		  if (rootConceptCode == null) return;
		  this.rootConceptCode = rootConceptCode;
	  }


	  public String selectFileAction() {
          // pop-up file selection dialog box (JNLP.jar)
          // update selectedFile
          // show selectedFile to the user

		  return "generate_standard_report";
	  }


      //public String addReportAction() {
	  public String saveTemplateAction() {
		  HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
          String codingSchemeNameAndVersion = (String) request.getSession().getAttribute("selectedOntology");
          String codingSchemeName = DataUtils.getCodingSchemeName(codingSchemeNameAndVersion);
          String codingSchemeVersion = DataUtils.getCodingSchemeVersion(codingSchemeNameAndVersion);
          String label = (String) request.getParameter("label");

          String rootConceptCode = (String) request.getParameter("rootConceptCode");
          String selectedAssociation = (String) request.getSession().getAttribute("selectedAssociation");
		  String selectedLevel = (String) request.getSession().getAttribute("selectedLevel");
		  String selectedDirection = (String) request.getSession().getAttribute("selectedDirection");
		  Boolean direction = null;

		  // return to error page
		  if (label == null || label.compareTo("") == 0)
		  {
			  KLO_log.warn("Incomplete data entry -- form not saved.");
			  return "add_standard_report_template";
		  }
		  if (rootConceptCode == null || rootConceptCode.compareTo("") == 0)
		  {
			  KLO_log.warn("Incomplete data entry -- form not saved.");
			  return "add_standard_report_template";
		  }
		  if (selectedLevel == null || selectedLevel.compareTo("") == 0)
		  {
			  KLO_log.warn("Incomplete data entry -- form not saved.");
			  return "add_standard_report_template";
		  }

		  if(selectedDirection.equals(Boolean.TRUE))
			  direction = true;
		  else
			  direction = false;

		  char delimiter = '$';
/*
		  KLO_log.warn("label: " + label);
          KLO_log.warn("codingSchemeName: " + codingSchemeName);
          KLO_log.warn("codingSchemeVersion: " + codingSchemeVersion);
          KLO_log.warn("rootConceptCode: " + rootConceptCode);
          KLO_log.warn("associationname: " + selectedAssociation);
          KLO_log.warn("direction: " + selectedDirection);
          KLO_log.warn("level: " + selectedLevel);
          KLO_log.warn("delimiter: " + delimiter);
*/
          // Save results using SDK writable API.
          try{
        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
        	  if(selectedLevel.equalsIgnoreCase("all"))
        		  selectedLevel = "-1";
  			  sdkclientutil.insertStandardReportTemplate(label, codingSchemeName, codingSchemeVersion, rootConceptCode, selectedAssociation, direction, Integer.parseInt(selectedLevel), delimiter);
  			  setSelectedStandardReportTemplate(label);
          } catch(Exception e) {
        	  e.printStackTrace();
          }

          //return "generate_standard_report";
          return "standard_report_template";
	  }



	  public String deleteReportTemplateAction() {
		  HttpServletRequest request = getHttpRequest();
		  String template_label = (String) request.getSession().getAttribute("selectedStandardReportTemplate");

		  KLO_log.warn("deleteReportTemplateAction: " + template_label);

          try{
			  StandardReportTemplate template = getStandardReportTemplate(template_label);
        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
  			  sdkclientutil.deleteStandardReportTemplate(template);

  			  //setSelectedStandardReportTemplate(label);
  			  List list = getStandardReportTemplateList();

          } catch(Exception e) {
        	  e.printStackTrace();
          }

		  return "standard_report_template";
	  }


      public StandardReportTemplate getStandardReportTemplate(String label) {
          try{
        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
        	  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
        	  String methodName = "setLabel";
        	  Object obj = sdkclientutil.search(FQName, methodName, label);
			  StandardReportTemplate standardReportTemplate = (StandardReportTemplate) obj;
			  return standardReportTemplate;
          } catch(Exception e) {
        	  e.printStackTrace();
          }
          return null;
      }


      public ReportColumn getReportColumn(int id) {
          try{
        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
        	  String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportColumn";
        	  String methodName = "setId";
        	  Object obj = sdkclientutil.search(FQName, methodName, id);
			  ReportColumn reportColumn = (ReportColumn) obj;
			  return reportColumn;
          } catch(Exception e) {
        	  e.printStackTrace();
          }
          return null;
      }


      //public String addReportColumnAction() {
	  public String saveReportColumnAction() {
		  HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

		  String fieldlabel = (String) request.getParameter("fieldlabel");
		  String columnNumber_str = (String) request.getParameter("columnNumber");
		  int columnNumber = Integer.parseInt(columnNumber_str);
  		  String fieldType = (String) request.getSession().getAttribute("selectedDataCategory");
  		  String propertyType = (String) request.getSession().getAttribute("selectedPropertyType");
  		  String propertyName = (String) request.getSession().getAttribute("selectedPropertyName");
  		  String representationalForm = (String) request.getSession().getAttribute("selectedRepresentationalForm");
		  String source = (String) request.getSession().getAttribute("selectedSource");
  		  String propertyQualifier = (String) request.getSession().getAttribute("selectedPropertyQualifier");
  		  String qualifierValue = (String) request.getParameter("qualifiervalue");

  		  String conditionalColumnId = (String) request.getParameter("dependentfield");
  		  int ccid = -1;

  		  if(conditionalColumnId != null)
  			if(conditionalColumnId != "")
  				ccid = Integer.parseInt(conditionalColumnId);

		  String preferred = (String) request.getParameter("preferred");
  		  Boolean isPreferred = null;
  		  if(preferred != null) {
			  if(preferred.equalsIgnoreCase("yes"))
				  isPreferred = Boolean.TRUE;
			  if(preferred.equalsIgnoreCase("no"))
				  isPreferred = Boolean.FALSE;
  		  }

/*
		  String selectedDirection = (String) request.getSession().getAttribute("selectedDirection");
		  Boolean direction = null;
		  if(selectedDirection != null) {
			  if(selectedDirection.equals(Boolean.TRUE))
				  direction = Boolean.TRUE;
			  else
				  direction = Boolean.FALSE;
		  }
*/
		  String delim = (String) request.getSession().getAttribute("selectedDelimiter");
		  char delimiter = ' ';
		  if(delim != null)
		  {
			  if(delim.length() > 0)
			  {
				  delimiter = delim.charAt(0);
			  }
		  }
/*
		  KLO_log.warn("fieldlabel: " + fieldlabel);
          KLO_log.warn("fieldType: " + fieldType);
          KLO_log.warn("propertyType: " + propertyType);
          KLO_log.warn("propertyName: " + propertyName);
          KLO_log.warn("isPreferred: " + isPreferred);
          KLO_log.warn("representationalForm: " + representationalForm);
          KLO_log.warn("source: " + source);
          KLO_log.warn("qualifierName: " + propertyQualifier);
          KLO_log.warn("qualifierValue: " + qualifierValue);

          //KLO_log.warn("direction: " + direction);
          KLO_log.warn("delimiter: " + delimiter);
          KLO_log.warn("ccid: " + ccid);
*/
          // Save results using SDK writable API.
          try{
        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
        	  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
        	  String methodName = "setLabel";
        	  String key = this.selectedStandardReportTemplate;
        	  //Object search(String FQName, String methodName, String key)
        	  Object obj = sdkclientutil.search(FQName, methodName, key);

        	  ReportColumn col = sdkclientutil.createReportColumn(fieldlabel, columnNumber, fieldType, propertyType, propertyName, isPreferred, representationalForm, source, propertyQualifier, qualifierValue, delimiter, ccid);
			  StandardReportTemplate standardReportTemplate = (StandardReportTemplate) obj;
			  col.setReportTemplate(standardReportTemplate);
              sdkclientutil.insertReportColumn(col);
              //setSelectedStandardReportTemplate(standardReportTemplate.getLabel());
        	  //sdkclientutil.insertReportColumn(fieldlabel, fieldType, propertyType, propertyName, isPreferred, representationalForm, source, propertyQualifier, qualifierValue, delimiter, ccid);
        	  //sdkclientutil.testGetTemplateCollection();

              request.getSession().setAttribute("selectedStandardReportTemplate", selectedStandardReportTemplate);


          } catch(Exception e) {
        	  e.printStackTrace();
          }
          return "standard_report_column";
	  }


	public List getReportStatusList() {
		reportStatusListData = DataUtils.getReportStatusListData();
		reportStatusList = new ArrayList();
		for (int i=0; i<reportStatusListData.size(); i++) {
			String t = (String) reportStatusListData.elementAt(i);
			reportStatusList.add(new SelectItem(t));
		}
		if (reportStatusList != null && reportStatusList.size() > 0) {
			selectedReportStatus = ((SelectItem) reportStatusList.get(0)).getLabel();
		}

		return reportStatusList;
	}

	public void setSelectedReportStatus(String selectedReportStatus) {
		this.selectedReportStatus = selectedReportStatus;
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute("selectedReportStatus", selectedReportStatus);
	}


	public String getSelectedReportStatus() {
		return this.selectedReportStatus;
	}

	public void reportStatusSelectionChanged(ValueChangeEvent event) {
		if (event.getNewValue() == null) return;
		setSelectedReportStatus(selectedReportStatus);
	}

    public String addStatusAction() {
		// to be modified
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String statusValue = (String) request.getParameter("statusValue");
		// save to database

		return "report_status";
	}

    public String assignStatusAction() {
		// to be modified
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		// save to database
		String reportTemplate = (String) request.getSession().getAttribute("selectedStandardReportTemplate");
		String statusValue = (String) request.getSession().getAttribute("selectedReportStatus");
		try{
      	  SDKClientUtil sdkclientutil = new SDKClientUtil();
      	  sdkclientutil.insertReportStatus(statusValue, reportTemplate, true);
      	  //sdkclientutil.testGetTemplateCollection();
        } catch(Exception e) {
      	  e.printStackTrace();
        }

		return "assign_report_status";
	}


	  public String modifyReportTemplateAction() {
		  HttpServletRequest request = getHttpRequest();
		  request.getSession().setAttribute("selectedStandardReportTemplate", selectedStandardReportTemplate);

          StandardReportTemplate standardReportTemplate = getStandardReportTemplate(selectedStandardReportTemplate);
          String ontologyNameAndVersion = standardReportTemplate.getCodingSchemeName() + " (version: " + standardReportTemplate.getCodingSchemeVersion() + ")";
          request.getSession().setAttribute("selectedOntology", ontologyNameAndVersion);

		  return "standard_report_column";
	  }


	  public String generateStandardReportAction() {
		  HttpServletRequest request = getHttpRequest();
		  request.getSession().setAttribute("selectedStandardReportTemplate", selectedStandardReportTemplate);

          StandardReportTemplate standardReportTemplate = getStandardReportTemplate(selectedStandardReportTemplate);
          //String ontologyNameAndVersion = standardReportTemplate.getCodingSchemeName() + " (version: " + standardReportTemplate.getCodingSchemeVersion() + ")";

          //request.getSession().setAttribute("selectedOntology", ontologyNameAndVersion);

		  return "generate_standard_report";
	  }

	  public String downloadReportAction() {
		  HttpServletRequest request = getHttpRequest();
		  request.getSession().setAttribute("selectedStandardReportTemplate", selectedStandardReportTemplate);

          StandardReportTemplate standardReportTemplate = getStandardReportTemplate(selectedStandardReportTemplate);
          //String ontologyNameAndVersion = standardReportTemplate.getCodingSchemeName() + " (version: " + standardReportTemplate.getCodingSchemeVersion() + ")";

System.out.println("downloading report " + selectedStandardReportTemplate);


          File dir = new File(download_dir);
          if (!dir.exists())
          {
System.out.println("Unable to download the specified report -- download directory does not exist. ");
               // create an error page (general purpose with the message being a session parameter)
		  }

		  File[] fileList = dir.listFiles();
		  int len=fileList.length;
		  while (len > 0) {
				len--;
				if (!fileList[len].isDirectory()) {
					String name = fileList[len].getName();
					System.out.println("File found in the download directory: " + name);
				}
		  }

          //request.getSession().setAttribute("selectedOntology", ontologyNameAndVersion);

          // find available reports in the download directory

          // for each file in the directory, find if the report has been approved.
          // if not, display a message page indicating such.


          // otherwise, route to generate_standard_report


		  return "generate_standard_report";
	  }


  }
