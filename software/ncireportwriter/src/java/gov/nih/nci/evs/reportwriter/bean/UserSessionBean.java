/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.bean;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.webapp.*;
import gov.nih.nci.evs.utils.*;

import java.io.*;
import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 *
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class UserSessionBean extends Object {
    private static Logger _logger = Logger.getLogger(UserSessionBean.class);
    private TaskRequest _taskRequest = new TaskRequest();
    private StandardReportTemplateManager _srtMgr =
        new StandardReportTemplateManager();

    private String _selectedPropertyType = null;
    private String _rootConceptCode = null;
    private String _selectedOntology = null;

    private String _selectedReportStatus = null;
    private List<SelectItem> _reportStatusList = null;
    private Vector<String> _reportStatusListData = null;

    private String _selectedReportFormat = null;
    private List<SelectItem> _reportFormatList = null;
    private Vector<String> _reportFormatListData = null;

    public String getSelectedPropertyType() {
        return _selectedPropertyType;
    }

    public void setSelectedPropertyType(String selectedPropertyType) {
        _selectedPropertyType = selectedPropertyType;
        HttpServletRequest request = HTTPUtils.getRequest();
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

    public List<SelectItem> getPropertyTypeList() {
        List<SelectItem> list = DataUtils.getPropertyTypeList();
        return list;
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

    public void taskSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String task = (String) event.getNewValue();
        setSelectedTask(task);
    }

    public String getRootConceptCode() {
        return _rootConceptCode;
    }

    public void setRootConceptCode(String rootConceptCode) {
        if (rootConceptCode == null)
            return;
        _rootConceptCode = rootConceptCode;
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
        HttpServletRequest request = HTTPUtils.getRequest();
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
        HttpServletRequest request = HTTPUtils.getRequest();
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

    // -------------------------------------------------------------------------
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
        HttpServletRequest request = HTTPUtils.getRequest();
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
    public StandardReportTemplate getStandardReportTemplate(String label) {
        return _srtMgr.getStandardReportTemplate(label);
    }

    public List<SelectItem> getStandardReportTemplateList() {
        return _srtMgr.getStandardReportTemplateList();
    }

    public String getSelectedStandardReportTemplate() {
        return _srtMgr.getSelectedStandardReportTemplate();
    }

    public void setSelectedStandardReportTemplate(
        String selectedStandardReportTemplate) {
        _srtMgr.setSelectedStandardReportTemplate(
            selectedStandardReportTemplate);
    }
    public String getSelectedStandardReportTemplate_draft() {
        return _srtMgr.getSelectedStandardReportTemplate_draft();
    }

    public void setSelectedStandardReportTemplate_draft(
        String selectedStandardReportTemplate_draft) {
        _srtMgr.setSelectedStandardReportTemplate_draft(
            selectedStandardReportTemplate_draft);
    }

    public List<SelectItem> getStandardReportTemplateList_draft() {
        return _srtMgr.getStandardReportTemplateList_draft();
    }

    public String getSelectedStandardReportTemplate_approved() {
        return _srtMgr.getSelectedStandardReportTemplate_approved();
    }

    public void setSelectedStandardReportTemplate_approved(
        String selectedStandardReportTemplate_draft) {
        _srtMgr.setSelectedStandardReportTemplate_approved(
            selectedStandardReportTemplate_draft);
    }

    public List<SelectItem> getStandardReportTemplateList_approved() {
        return _srtMgr.getStandardReportTemplateList_approved();
    }

    // -------------------------------------------------------------------------
    public String performTask() {
        return _taskRequest.performAction();
    }

    public String getSelectedTask() {
        return _taskRequest.getSelectedTask();
    }

    public void setSelectedTask(String selectedTask) {
        _taskRequest.setSelectedTask(selectedTask);
    }

    public void changeTaskSelection(ValueChangeEvent vce) {
        _taskRequest.changeTaskSelection(vce);
    }

    public List<SelectItem> getTaskList() {
        return _taskRequest.getTaskList();
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

    public String selectGenerateReportOptionAction() {
        return "selectGenerateReportOption";
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
            .saveAction(_srtMgr.getSelected());
    }

    public String saveModifiedReportColumnAction() {
        return new ReportColumnRequest()
            .saveModifiedAction(_srtMgr.getSelected());
    }

    // -------------------------------------------------------------------------
    public String editReportContentAction() {
        return new ReportContentRequest()
            .editAction(_srtMgr.getSelected());
    }

    public String generateStandardReportAction() {
        return new ReportContentRequest()
            .generateAction(_srtMgr.getSelected());
    }

    public String displayStandardReportTemplateAction() {
        return "standard_report_template";
    }

    // -------------------------------------------------------------------------
    public String addStatusAction() {
    	return new ReportStatusRequest().addAction();
    }

    public String activateStatusAction () {
        return new ReportStatusRequest().activateAction();
    }

    public String inactivateStatusAction () {
        return new ReportStatusRequest().inactivateAction();
    }

    public String assignStatusAction() {
    	return new ReportStatusRequest().assignAction();
    }

    public String saveStatusAction() { // Might not be called.
        return new ReportStatusRequest().
            saveAction(_srtMgr.getSelected());
    }

    // -------------------------------------------------------------------------
    public String downloadReportAction() {
        return new ReportDownloadRequest()
            .downloadReportAction(_srtMgr.getSelected());
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

    public String enterExcelMetadata() {
        String selectedStandardReportTemplate = _srtMgr.getSelected();
        System.out.println("selectedStandardReportTemplate: " + selectedStandardReportTemplate);
        HttpServletRequest request = HTTPUtils.getRequest();
        String author = request.getParameter("author");
        System.out.println("author: " + author);
        String keywords = request.getParameter("keywords");
        System.out.println("keywords: " + keywords);
        String title = request.getParameter("title");
        System.out.println("title: " + title);
        String subject = request.getParameter("subject");
        System.out.println("subject: " + subject);
        String worksheet = request.getParameter("worksheet");
        worksheet = worksheet.trim();
        System.out.println("worksheet: " + worksheet);
        String frozen_rows = request.getParameter("frozen_rows");
        System.out.println("frozen_rows: " + frozen_rows);
        frozen_rows = frozen_rows.trim();

        request.getSession().setAttribute("author", author);
        request.getSession().setAttribute("keywords", keywords);
        request.getSession().setAttribute("title", title);
        request.getSession().setAttribute("subject", subject);
        request.getSession().setAttribute("worksheet", worksheet);
        request.getSession().setAttribute("frozen_rows", frozen_rows);

        String format_description = "Microsoft Office Excel";
		String hibernate_cfg_xml = request.getSession().getServletContext().getRealPath(JDBCUtil.HIBERNATE_CFG_PATH);//"/WEB-INF/classes/hibernate.cfg.xml");
		File f = new File(hibernate_cfg_xml);
		if (f.exists()) {
			JDBCUtil util = new JDBCUtil(hibernate_cfg_xml);
			int templateId = util.getTemplateId(selectedStandardReportTemplate);
			System.out.println("templateId: " + templateId);
			Vector<Integer> reportIds = util.getReportIds(templateId);
			if (reportIds != null) {
	             Vector reports = util.getReportData(reportIds, format_description);
	             System.out.println("Number of excel files: " + reports.size());
	             if (reports != null) {
					 int success_knt = 0;
					 for (int i=0; i<reports.size(); i++) {
						 ReportMetadata mrd = (ReportMetadata) reports.elementAt(i);
						 String sourcefile = mrd.getPathName();
						 if (sourcefile != null) {
							 System.out.println("sourcefile: " + sourcefile);
							 ExcelMetadataUtils.updateMetadata(sourcefile, author, keywords, title, subject);
							 if (worksheet.length() > 0 && frozen_rows.length() > 0) {
								 int worksheet_number = Integer.parseInt(worksheet);
								 int frozen_rows_number = Integer.parseInt(frozen_rows);
								 boolean success = ExcelMetadataUtils.freezeRow(sourcefile, worksheet_number, frozen_rows_number);
								 if (success) {
									 success_knt++;
								 }
							 }
						 }
					 }

					 if (success_knt < reports.size()) {
						  String msg = "Please verify input values and try again.";
						  request.getSession().setAttribute("warningMsg", msg);
						  return "failed";
					 }
				 }
			}
		}
        return "success";
	}
}
