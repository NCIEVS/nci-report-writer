/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;

import java.util.*;

import javax.servlet.http.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class ReportStatusRequest {
    public String addAction() {
        HttpServletRequest request = HTTPUtils.getRequest();
        return HTTPUtils.warningMsg(request, "Not Yet Implemented.");
        //return "report_status";
    }
    
    public String activateAction() {
        HttpServletRequest request = HTTPUtils.getRequest();
        return HTTPUtils.warningMsg(request, "Not Yet Implemented.");        
    }

    public String inactivateAction() {
        HttpServletRequest request = HTTPUtils.getRequest();
        return HTTPUtils.warningMsg(request, "Not Yet Implemented.");
    }

    public String saveAction(String selectedStandardReportTemplate) {
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
                selectedStandardReportTemplate);

        UserSessionBean usBean = BeanUtils.getUserSessionBean();
        StandardReportTemplate standardReportTemplate =
            usBean.getStandardReportTemplate(selectedStandardReportTemplate);

        return HTTPUtils.sessionMsg(request, "The status of the "
                + standardReportTemplate.getLabel()
                + " has been updated successfully.");
    }

    public String assignAction() {
        HttpServletRequest request = HTTPUtils.getRequest();
        // save to database
        String reportTemplate =
            (String) request.getSession().getAttribute(
                    "selectedStandardReportTemplate_draft");
        String statusValue =
            (String) request.getSession().getAttribute("selectedReportStatus");

        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            Object[] objs = sdkclientutil.search(FQName);
            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    StandardReport standardReport = (StandardReport) objs[i];
                    standardReportTemplate = standardReport.getTemplate();
                    if (standardReportTemplate != null) {
                        if (reportTemplate.compareTo(standardReportTemplate
                                .getLabel()) == 0) {
                            FQName =
                                "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
                            String methodName = "setLabel";
                            String key = statusValue;

                            Object status_obj =
                                sdkclientutil.search(FQName, methodName, key);
                            if (status_obj != null) {
                                standardReport
                                        .setStatus((ReportStatus) status_obj);
                                java.util.Date lastModified = new Date(); // system
                                // date
                                standardReport.setLastModified(lastModified);
                                sdkclientutil
                                        .updateStandardReport(standardReport);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserSessionBean usBean = BeanUtils.getUserSessionBean();
        usBean.getStandardReportTemplateList_draft();

        return "assign_report_status";
    }
}
