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

import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

/**
 *
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
        HttpServletRequest request = HTTPUtils.getRequest();
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

        } else if (_selectedTask.compareTo("Administer Excel Metadata") == 0) {
            return "administer_excel_metadata";

        } else if (_selectedTask.compareTo("Generate Hierarchy Report") == 0) {
            return "generate_hierarchy_report";

        } else if (_selectedTask.compareTo("Assign Report Status") == 0) {
            // Check if there is any DRAFT report waiting for approval:
            List<SelectItem> list =
                usBean.getStandardReportTemplateList_draft();
            if (list != null && list.size() > 0) {
                return "assign_report_status";
            } else {
                HttpServletRequest request = HTTPUtils.getRequest();
                return HTTPUtils.sessionMsg(request,
                        "No draft report is found.");
            }
        } else if (_selectedTask.compareTo("Retrieve Standard Reports") == 0) {
            HttpServletRequest request = HTTPUtils.getRequest();
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
