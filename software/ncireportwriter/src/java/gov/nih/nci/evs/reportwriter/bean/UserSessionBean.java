package gov.nih.nci.evs.reportwriter.bean;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.webapp.*;

import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
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
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class UserSessionBean extends Object {
    private static Logger _logger = Logger.getLogger(UserSessionBean.class);

    private Boolean _isAdmin = null;
    private String _selectedTask = null;

    // for templates with reports already been generated
    private List<SelectItem> _standardReportTemplateList_draft =
        new ArrayList<SelectItem>();
    // for templates with reports already been generated
    private List<SelectItem> _standardReportTemplateList_approved =
        new ArrayList<SelectItem>();

    private String _selectedStandardReportTemplate = null;
    private String _selectedStandardReportTemplate_draft = null;
    private String _selectedStandardReportTemplate_approved = null;

    private String _selectedPropertyType = null;
    private String _rootConceptCode = null;
    private String _selectedOntology = null;

    private String _selectedReportStatus = null;
    private List<SelectItem> _reportStatusList = null;
    private Vector<String> _reportStatusListData = null;

    private String _selectedReportFormat = null;
    private List<SelectItem> _reportFormatList = null;
    private Vector<String> _reportFormatListData = null;

    public void setIsAdmin(Boolean isAdmin) {
        _isAdmin = isAdmin;
    }

    public Boolean getIsAdmin() {
        return _isAdmin;
    }

    public String getSelectedTask() {
        return _selectedTask;
    }

    public void setSelectedTask(String selectedTask) {
        _selectedTask = selectedTask;
    }

    public String getSelectedPropertyType() {
        return _selectedPropertyType;
    }

    public void setSelectedPropertyType(String selectedPropertyType) {
        _selectedPropertyType = selectedPropertyType;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedPropertyType",
            selectedPropertyType);
    }

    public void propertyTypeSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedPropertyType(newValue);
    }

    public String getSelectedOntology() {
        return _selectedOntology;
    }

    public void setSelectedOntology(String selectedOntology) {
        _selectedOntology = selectedOntology;
    }

    public List<SelectItem> getTaskList() {
        HttpServletRequest request = SessionUtil.getRequest();
        HttpSession session = request.getSession(false);

        Boolean isAdmin = null;
        if (session != null) {
            isAdmin =
                (Boolean) request.getSession(true).getAttribute("isAdmin");
        }

        List<SelectItem> list = DataUtils.getTaskList(isAdmin);
        if (list != null) {
            SelectItem item = (SelectItem) list.get(0);
            _selectedTask = item.getLabel();
        }
        // return DataUtils.getTaskList(isAdmin);
        return list;
    }

    public List<SelectItem> getPropertyTypeList() {
        List<SelectItem> list = DataUtils.getPropertyTypeList();
        return list;
    }

    public void changeTaskSelection(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        // logger.debug("========== changeTaskSelection " + newValue);
        _selectedTask = newValue;
    }

    public void reportSelectionChanged(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedStandardReportTemplate(newValue);
    }

    public void reportSelectionChanged_draft(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedStandardReportTemplate_draft(newValue);
    }

    public void reportSelectionChanged_approved(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedStandardReportTemplate_approved(newValue);
    }

    public List<SelectItem> getStandardReportTemplateList() {

        List<SelectItem> list = DataUtils.getStandardReportTemplateList();
        if (_selectedStandardReportTemplate == null) {
            if (list != null && list.size() > 0) {
                if (getSelectedStandardReportTemplate() == null) {
                    SelectItem item = (SelectItem) list.get(0);
                    setSelectedStandardReportTemplate(item.getLabel());
                }
            }
        }
        return list;
    }

    public String getSelectedStandardReportTemplate() {
        return _selectedStandardReportTemplate;
    }

    public void setSelectedStandardReportTemplate(
        String selectedStandardReportTemplate) {
        _selectedStandardReportTemplate = selectedStandardReportTemplate;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            selectedStandardReportTemplate);
    }
    
    public void setStandardReportTemplateList_draft(List<SelectItem> list) {
        _standardReportTemplateList_draft = list;
    }

    public List<SelectItem> getStandardReportTemplateList_draft() {
        // Find all templates with reports already been generated
        List<SelectItem> list = new ArrayList<SelectItem>();
        HashSet<String> hset = new HashSet<String>();
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            Object[] objs = sdkclientutil.search(FQName);

            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    StandardReport standardReport = (StandardReport) objs[i];
                    ReportStatus rs = standardReport.getStatus();
                    String status = rs.getLabel();
                    standardReportTemplate = standardReport.getTemplate();
                    if (standardReportTemplate != null) {
                        if (status.compareToIgnoreCase("DRAFT") == 0) {
                            if (!hset.contains(standardReportTemplate
                                .getLabel())) {
                                hset.add(standardReportTemplate.getLabel());
                                list.add(new SelectItem(standardReportTemplate
                                    .getLabel()));
                            }
                        }
                    }
                }

                if (list != null && list.size() > 0) {
                    SelectItem item = list.get(0);
                    setSelectedStandardReportTemplate_draft(item.getLabel());
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public String getSelectedStandardReportTemplate_draft() {
        return _selectedStandardReportTemplate_draft;
    }

    public void setSelectedStandardReportTemplate_draft(
        String selectedStandardReportTemplate_draft) {
        _selectedStandardReportTemplate_draft =
            selectedStandardReportTemplate_draft;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute(
            "selectedStandardReportTemplate_draft",
            selectedStandardReportTemplate_draft);
    }

    public List<SelectItem> getStandardReportTemplateList_approved() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        HashSet<String> hset = new HashSet<String>();
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            Object[] objs = sdkclientutil.search(FQName);
            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    StandardReport standardReport = (StandardReport) objs[i];
                    ReportStatus rs = standardReport.getStatus();
                    String status = rs.getLabel();
                    if (status.compareTo("APPROVED") == 0) {
                        standardReportTemplate = standardReport.getTemplate();
                        if (!hset.contains(standardReportTemplate.getLabel())) {
                            hset.add(standardReportTemplate.getLabel());
                            list.add(new SelectItem(standardReportTemplate
                                .getLabel()));
                        }
                    }
                }

                if (list == null) {
                    if (list != null && list.size() > 0) {
                        SelectItem item = list.get(0);
                        setSelectedStandardReportTemplate_approved(item
                            .getLabel());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public String getSelectedStandardReportTemplate_approved() {
        return _selectedStandardReportTemplate_approved;
    }

    public void setSelectedStandardReportTemplate_approved(
        String selectedStandardReportTemplate_draft) {
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute(
            "selectedStandardReportTemplate_approved",
            _selectedStandardReportTemplate_approved);
    }

    public void taskSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String task = (String) event.getNewValue();
        setSelectedTask(task);
    }

    public String performTask() {
        if (_selectedTask.compareTo("Administer Standard Reports") == 0) {
            List<SelectItem> list = getStandardReportTemplateList();
            if (list == null || list.size() == 0) {
                return "add_standard_report_template";
            }
            return "administer_standard_reports";
        } else if (_selectedTask.compareTo("Maintain Report Status") == 0) {
            return "report_status";
        } else if (_selectedTask.compareTo("Assign Report Status") == 0) {
            // Check if there is any DRAFT report waiting for approval:
            _standardReportTemplateList_draft =
                getStandardReportTemplateList_draft();
            if (_standardReportTemplateList_draft != null
                && _standardReportTemplateList_draft.size() > 0) {
                return "assign_report_status";
            } else {
                HttpServletRequest request = SessionUtil.getRequest();
                return HTTPUtils.sessionMsg(request, "No draft report is found.");
            }
        } else if (_selectedTask.compareTo("Retrieve Standard Reports") == 0) {
            HttpServletRequest request = SessionUtil.getRequest();
            Boolean isAdmin =
                (Boolean) request.getSession().getAttribute("isAdmin");
            if (isAdmin != null && isAdmin.equals(Boolean.TRUE)) {
                return "retrieve_standard_reports";
            } else {
                // Check if there is any APPROVED report waiting for approval:
                _standardReportTemplateList_approved =
                    getStandardReportTemplateList_approved();
                if (_standardReportTemplateList_approved != null
                    && _standardReportTemplateList_approved.size() > 0) {
                    return "retrieve_standard_reports";
                } else {
                    return HTTPUtils.sessionMsg(request, "No approved report is found. ");
                }
            }
        } else if (_selectedTask.compareTo("Unlock User Account") == 0) {
            return "perform_unlock";
        }
        return null;
    }

    public String getRootConceptCode() {
        return _rootConceptCode;
    }

    public void setRootConceptCode(String rootConceptCode) {
        if (rootConceptCode == null)
            return;
        _rootConceptCode = rootConceptCode;
    }

    public StandardReportTemplate getStandardReportTemplate(String label) {
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            Object obj = sdkclientutil.search(FQName, methodName, label);
            StandardReportTemplate standardReportTemplate =
                (StandardReportTemplate) obj;
            return standardReportTemplate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SelectItem> getReportFormatList() {
        _reportFormatListData = DataUtils.getReportFormatListData();
        _reportFormatList = new ArrayList<SelectItem>();
        for (int i = 0; i < _reportFormatListData.size(); i++) {
            String t = _reportFormatListData.elementAt(i);
            _reportFormatList.add(new SelectItem(t));
        }
        if (_reportFormatList != null && _reportFormatList.size() > 0) {
            _selectedReportFormat = _reportFormatList.get(0).getLabel();
        }

        return _reportFormatList;
    }

    public void setSelectedReportFormat(String selectedReportFormat) {
        _selectedReportFormat = selectedReportFormat;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedReportFormat",
            selectedReportFormat);
    }

    public String getSelectedReportFormat() {
        return _selectedReportFormat;
    }

    public void ReportFormatSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        setSelectedReportFormat(_selectedReportFormat);
    }

    public List<SelectItem> getReportStatusList() {
        _reportStatusListData = DataUtils.getReportStatusListData();
        _reportStatusList = new ArrayList<SelectItem>();
        for (int i = 0; i < _reportStatusListData.size(); i++) {
            String t = _reportStatusListData.elementAt(i);
            _reportStatusList.add(new SelectItem(t));
        }
        if (_reportStatusList != null && _reportStatusList.size() > 0)
            _selectedReportStatus = _reportStatusList.get(0).getLabel();
        return _reportStatusList;
    }

    public void setSelectedReportStatus(String selectedReportStatus) {
        _selectedReportStatus = selectedReportStatus;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedReportStatus",
            selectedReportStatus);
    }

    public String getSelectedReportStatus() {
        return _selectedReportStatus;
    }

    public void reportStatusSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        setSelectedReportStatus(_selectedReportStatus);
    }

    private String _selectedVersion = null;
    private List<SelectItem> _versionList = null;
    private Vector<String> _versionListData = null;

    public List<SelectItem> getVersionList(String codingschemename) {
        _versionListData = DataUtils.getVersionListData(codingschemename);
        _versionList = new ArrayList<SelectItem>();
        for (int i = 0; i < _versionListData.size(); i++) {
            String t = _versionListData.elementAt(i);

            _logger.debug("version: " + t);

            _versionList.add(new SelectItem(t));
        }
        if (_versionList != null && _versionList.size() > 0) {
            _selectedVersion = _versionList.get(0).getLabel();
        }
        return _versionList;
    }

    public void setVersionList(List<SelectItem> list) {
        _versionList = list;
    }

    public void setSelectedVersion(String selectedVersion) {
        _selectedVersion = selectedVersion;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedVersion", selectedVersion);
    }

    public String getSelectedVersion() {
        return _selectedVersion;
    }

    public void versionSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        setSelectedVersion(_selectedVersion);
    }

    // -------------------------------------------------------------------------
    public String addReportTemplateAction() {
        return new ReportTemplateRequest().addAction();
    }

    public String modifyReportTemplateAction() {
        return new ReportTemplateRequest().modifyAction();
    }

    public String saveTemplateAction() {
        return new ReportTemplateRequest().saveAction();
    }

    public String saveModifiedTemplateAction() {
        return new ReportTemplateRequest().saveModifiedAction();
    }

    public String deleteReportTemplateAction() {
        return new ReportTemplateRequest().deleteAction();
    }

    // -------------------------------------------------------------------------
    public String addColumnAction() { // Might not be called.
        return new ReportColumnRequest().addAction();
    }

    public String modifyColumnAction() {
        return new ReportColumnRequest().modifyAction();
    }

    public String insertBeforeColumnAction() {
        return new ReportColumnRequest().insertBeforeAction();
    }

    public String insertAfterColumnAction() { // Might not be called.
        return new ReportColumnRequest().insertAfterAction();
    }

    public String deleteColumnAction() {
        return new ReportColumnRequest().deleteAction();
    }

    public String saveReportColumnAction() {
        return new ReportColumnRequest()
            .saveAction(_selectedStandardReportTemplate);
    }

    public String saveModifiedReportColumnAction() {
        return new ReportColumnRequest()
            .saveModifiedAction(_selectedStandardReportTemplate);
    }
    
    // -------------------------------------------------------------------------
    public String editReportContentAction() {
        return new ReportContentRequest()
            .editAction(_selectedStandardReportTemplate);
    }

    public String generateStandardReportAction() {
        return new ReportContentRequest()
            .generateAction(_selectedStandardReportTemplate);
    }

    // -------------------------------------------------------------------------
    public String addStatusAction() {
    	return new ReportStatusRequest().addAction();
    }

    public String assignStatusAction() {
    	return new ReportStatusRequest().assignAction();
    }
    
    public String saveStatusAction() { // Might not be called.
        return new ReportStatusRequest().
            saveAction(_selectedStandardReportTemplate);
    }
    
    // -------------------------------------------------------------------------
    public String downloadReportAction() {
        return new ReportDownloadRequest()
            .downloadReportAction(_selectedStandardReportTemplate);
    }
    
    // -------------------------------------------------------------------------
    public String submitAccessDenied() {
        return new AccessDeniedRequest().submit();
    }

    public String clearAccessDenied() {
        return new AccessDeniedRequest().clear();
    }

    public String submitContactUs() {
        return new ContactUsRequest().submit();
    }

    public String clearContactUs() {
        return new ContactUsRequest().clear();
    }

    public String submitUnlockAccount() {
        return new UserAccountRequest().unlock();
    }

    public String clearUnlockAccount() {
        return new UserAccountRequest().clear();
    }
}
