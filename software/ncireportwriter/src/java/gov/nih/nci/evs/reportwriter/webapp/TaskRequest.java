package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;

import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

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

public class TaskRequest {
    private String _selectedTask = null;

    public String getSelectedTask() {
        return _selectedTask;
    }

    public void setSelectedTask(String selectedTask) {
        _selectedTask = selectedTask;
    }

    public void changeTaskSelection(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        _selectedTask = newValue;
    }

    public List<SelectItem> getTaskList() {
        HttpServletRequest request = SessionUtils.getRequest();
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

    public String performAction() {
        UserSessionBean usBean = BeanUtils.getUserSessionBean();
        if (_selectedTask.compareTo("Administer Standard Reports") == 0) {
            List<SelectItem> list = usBean.getStandardReportTemplateList();
            if (list == null || list.size() == 0) {
                return "add_standard_report_template";
            }
            return "administer_standard_reports";
        } else if (_selectedTask.compareTo("Maintain Report Status") == 0) {
            return "report_status";
        } else if (_selectedTask.compareTo("Assign Report Status") == 0) {
            // Check if there is any DRAFT report waiting for approval:
            List<SelectItem> list =
                usBean.getStandardReportTemplateList_draft();
            if (list != null && list.size() > 0) {
                return "assign_report_status";
            } else {
                HttpServletRequest request = SessionUtils.getRequest();
                return HTTPUtils.sessionMsg(request,
                        "No draft report is found.");
            }
        } else if (_selectedTask.compareTo("Retrieve Standard Reports") == 0) {
            HttpServletRequest request = SessionUtils.getRequest();
            Boolean isAdmin =
                (Boolean) request.getSession().getAttribute("isAdmin");
            if (isAdmin != null && isAdmin.equals(Boolean.TRUE)) {
                return "retrieve_standard_reports";
            } else {
                // Check if there is any APPROVED report waiting for approval:
                List<SelectItem> list =
                    usBean.getStandardReportTemplateList_approved();
                if (list != null && list.size() > 0) {
                    return "retrieve_standard_reports";
                } else {
                    return HTTPUtils.sessionMsg(request,
                            "No approved report is found. ");
                }
            }
        } else if (_selectedTask.compareTo("Unlock User Account") == 0) {
            return "perform_unlock";
        }
        return null;
    }
}
