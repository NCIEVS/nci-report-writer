/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;

import java.io.*;
import java.util.*;

import javax.faces.model.*;
import javax.servlet.http.*;

/**
 *
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */
public class StandardReportTemplateManager {
    private String _selectedStandardReportTemplate = null;
    private String _selectedStandardReportTemplate_draft = null;
    private String _selectedStandardReportTemplate_approved = null;

    // -------------------------------------------------------------------------
    public String getSelected() {
        return _selectedStandardReportTemplate;
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
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            selectedStandardReportTemplate);
    }


    // -------------------------------------------------------------------------
    private List<SelectItem> getStandardReportTemplateList(String version) {
		HttpServletRequest request = HTTPUtils.getRequest();


        List<SelectItem> list = new ArrayList<SelectItem>();
        HashSet<String> hset = new HashSet<String>();

		String hibernate_cfg_xml = request.getSession().getServletContext().getRealPath(JDBCUtil.HIBERNATE_CFG_PATH);//"/WEB-INF/classes/hibernate.cfg.xml");
		File f = new File(hibernate_cfg_xml);
		if (f.exists()) {
			JDBCUtil util = new JDBCUtil(hibernate_cfg_xml);
			Vector report_metadata_vec = util.getReportData();

			for (int i=0; i<report_metadata_vec.size(); i++) {
				ReportMetadata rmd = (ReportMetadata) report_metadata_vec.elementAt(i);
				if (rmd.getStatus().compareTo(version) == 0) {
                    String templateLabel = rmd.getTemplateLabel();
                    if (!hset.contains(templateLabel)) {
						hset.add(templateLabel);
						list.add(new SelectItem(templateLabel));
					}
				}
			}
		}
		return list;
    }

/*
    private List<SelectItem> getStandardReportTemplateList(String version) {

        List<SelectItem> list = new ArrayList<SelectItem>();
        HashSet<String> hset = new HashSet<String>();
        SDKClientUtil sdkclientutil = new SDKClientUtil();
        StandardReportTemplate standardReportTemplate = null;
        String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";

        Object[] objs = null;
        try {
        	objs = sdkclientutil.search(FQName);
		} catch (Exception ex) {
			System.out.println("Exception at sdkclientutil.search(FQName): " + FQName);
		}


        if (objs == null || objs.length <= 0) {
            return list;
	    }

        for (int i = 0; i < objs.length; i++) {
            StandardReport standardReport = (StandardReport) objs[i];
            if (standardReport != null) {
				ReportStatus rs = null;
				try {
					rs = standardReport.getStatus();
				} catch (Exception ex) {
					System.out.println("standardReport.getStatus() exception?");
				}

				if (rs != null) {
					String status = rs.getLabel();
                    standardReportTemplate = null;
                    try {
						standardReportTemplate = standardReport.getTemplate();
					} catch (Exception ex) {

					}

					if (standardReportTemplate == null ||
						!status.equalsIgnoreCase(version) ||
						hset.contains(standardReportTemplate.getLabel()))
						continue;

					hset.add(standardReportTemplate.getLabel());
					list.add(new SelectItem(standardReportTemplate.getLabel()));
			    }
		    }
        }
        return list;
    }
*/

    // -------------------------------------------------------------------------
    public String getSelectedStandardReportTemplate_draft() {
        return _selectedStandardReportTemplate_draft;
    }

    public void setSelectedStandardReportTemplate_draft(
        String selectedStandardReportTemplate_draft) {
        _selectedStandardReportTemplate_draft =
            selectedStandardReportTemplate_draft;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute(
            "selectedStandardReportTemplate_draft",
            selectedStandardReportTemplate_draft);
    }

    public List<SelectItem> getStandardReportTemplateList_draft() {
        List<SelectItem> list = null;

        list = getStandardReportTemplateList("DRAFT");
        if (list != null && list.size() > 0) {
            SelectItem item = list.get(0);
            setSelectedStandardReportTemplate_draft(item.getLabel());
            SortUtils.quickSort(list);
        }

        return list;
    }

    // -------------------------------------------------------------------------
    public String getSelectedStandardReportTemplate_approved() {
        return _selectedStandardReportTemplate_approved;
    }

    public void setSelectedStandardReportTemplate_approved(
        String selectedStandardReportTemplate_draft) {
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute(
            "selectedStandardReportTemplate_approved",
            _selectedStandardReportTemplate_approved);
    }

    public List<SelectItem> getStandardReportTemplateList_approved() {
        List<SelectItem> list = getStandardReportTemplateList("APPROVED");
        if (list == null) {
            if (list != null && list.size() > 0) {
                SelectItem item = list.get(0);
                setSelectedStandardReportTemplate_approved(item.getLabel());
            }
        }
        SortUtils.quickSort(list);
        return list;
    }
}
