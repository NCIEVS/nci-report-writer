/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.webapp;

import java.util.*;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.service.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;

import javax.servlet.http.*;

import org.LexGrid.codingSchemes.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class ReportContentRequest {
    private static Logger _logger =
        Logger.getLogger(ReportContentRequest.class);
    private StandardReportTemplate _standardReportTemplate = null;
    private String _selectedStandardReportTemplate = null;

    // -------------------------------------------------------------------------
    private void init(String selectedStandardReportTemplate) {
        _selectedStandardReportTemplate = selectedStandardReportTemplate;
        UserSessionBean usBean = BeanUtils.getUserSessionBean();
        _standardReportTemplate =
            usBean
                .getStandardReportTemplate(_selectedStandardReportTemplate);
    }

    // -------------------------------------------------------------------------
    public String displayCodingSchemeWarning(HttpServletRequest request) {
        String csn = _standardReportTemplate.getCodingSchemeName();
        String version = _standardReportTemplate.getCodingSchemeVersion();
        String csnv = DataUtils.getCSNVKey(csn, version);

        String versionTmp = DataUtils.getCodingSchemeVersion(csnv);
        if (versionTmp != null)
            return null;

        CodingScheme cs = DataUtils.getCodingScheme(csn);
        if (cs == null)
            return HTTPUtils.warningMsg(request,
                "The following vocabulary is not loaded:\n" + "    * " + csnv);

        versionTmp = cs.getRepresentsVersion();
        String csnvLatest = DataUtils.getCSNVKey(csn, versionTmp);
        String msg = "";
        msg += "The selected report template is referencing an older";
        msg += " or invalid version of the coding scheme:\n";
        msg += "    * Current version: " + csnv + "\n";
        msg += "    * Latest version: " + csnvLatest + "\n";
        msg += "\n";
        msg += "Please update the version number of the coding scheme";
        msg += " by selecting the Modify button.";
        return HTTPUtils.warningMsg(request, msg);
    }

    // -------------------------------------------------------------------------
    public String editAction(String selectedStandardReportTemplate) {
        init(selectedStandardReportTemplate);
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            _selectedStandardReportTemplate);

        String csn = _standardReportTemplate.getCodingSchemeName();
        String version = _standardReportTemplate.getCodingSchemeVersion();
        String csnv = DataUtils.getCSNVKey(csn, version);
        request.getSession().setAttribute("selectedOntology", csnv);

        String warningMsg = displayCodingSchemeWarning(request);
        return warningMsg == null ? "standard_report_column" : warningMsg;
    }

    public String generateAction(String selectedStandardReportTemplate) {
        init(selectedStandardReportTemplate);
        HttpServletRequest request = HTTPUtils.getRequest();
        String warningMsg = displayCodingSchemeWarning(request);
        StringBuffer buffer = new StringBuffer();
        if (warningMsg != null)
            return warningMsg;

        String templateId =
            (String) request.getSession().getAttribute(
                "selectedStandardReportTemplate");
        _logger.debug("generateStandardReportAction: " + templateId);

        String defining_set_desc = null;
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            String key = templateId;
            Object standardReportTemplate_obj =
                sdkclientutil.search(FQName, methodName, key);
            if (standardReportTemplate_obj != null) {
                standardReportTemplate =
                    (StandardReportTemplate) standardReportTemplate_obj;

                String codingscheme =
                    standardReportTemplate.getCodingSchemeName();
                String version =
                    standardReportTemplate.getCodingSchemeVersion();

                _logger.debug("generateStandardReportAction: codingscheme "
                    + codingscheme);
                _logger.debug("generateStandardReportAction: version "
                    + version);

                if (!DataUtils.isValidCodingScheme(codingscheme, version)) {
                    buffer.append("Invalid coding scheme name " + codingscheme);
                    buffer.append(" or version " + version + ".\n");
                    buffer.append("The report template may be out of date.  ");
                    buffer.append("Please modify it and resubmit.");
                    return HTTPUtils.sessionMsg(request, buffer);
                }

                defining_set_desc = standardReportTemplate.getRootConceptCode();
                String rootConceptCode = null;
                if (defining_set_desc.indexOf("|") == -1) {
                    rootConceptCode =
                        standardReportTemplate.getRootConceptCode();
                    String ltag = null;
                    Concept rootConcept =
                        DataUtils.getConceptByCode(codingscheme, version, ltag,
                            rootConceptCode);
                    if (rootConcept == null) {
                        buffer.append("Invalid root concept code: ");
                        buffer.append(rootConceptCode + ".\n");
                        buffer.append("Please modify the report template");
                        buffer.append(" and resubmit.");
                        return HTTPUtils.sessionMsg(request, buffer);
                    }
                    String associationName =
                        standardReportTemplate.getAssociationName();
                    key = codingscheme + " (version: " + version + ")";
                    Vector<String> associationname_vec =
                        DataUtils.getSupportedAssociations(
                            DataUtils.AssociationType.Names, key);
                    if (!associationname_vec.contains(associationName)) {
                        buffer.append("Invalid association name: ");
                        buffer.append(associationName + ".\n");
                        buffer.append("Please modify the report template");
                        buffer.append(" and resubmit.");
                        return HTTPUtils.sessionMsg(request, buffer);
                    }
                }
            }
        } catch (Exception ex) {
            return HTTPUtils.sessionMsg(request,
                "Exception encountered when generating " + templateId + ".");
        }

        String uid = (String) request.getSession().getAttribute("uid");
        if (uid == null)
            return HTTPUtils.sessionMsg(request,
                "You must first login to perform this function.");

        String reportFormat_value = "Text (tab delimited)";
        String reportStatus_value = "DRAFT";

        String message =
            StandardReportService.validReport(_selectedStandardReportTemplate,
                reportFormat_value, reportStatus_value, uid);
        if (message.compareTo("success") != 0)
            return HTTPUtils.sessionMsg(request, message);

        String download_dir =
            AppProperties.getInstance().getProperty(
                AppProperties.REPORT_DOWNLOAD_DIRECTORY);
        _logger.debug("download_dir " + download_dir);
        if (download_dir == null) {
            buffer.append("The download directory has not been set");
            buffer.append(" up properly.\n");
            buffer.append("Ask your administrator to check the JBoss");
            buffer.append(" setting in properties-service.xml.");
            return HTTPUtils.sessionMsg(request, buffer);
        }

        String emailAddress =
            (String) request.getSession().getAttribute("email");
        _logger.debug("emailAddress: " + emailAddress);
        StandardReportService.generateStandardReport(download_dir,
            _selectedStandardReportTemplate, uid, emailAddress);
        buffer.append("Your request has been received.  ");
        buffer.append("The report, " + templateId + ", in tab-delimited");
        buffer.append(" and Microsft Excel formats will be generated");
        buffer.append(" and placed in the designated output directory.  ");
        buffer.append("Please review and assign an APPROVED status");
        buffer.append(" before making it available to the users.");
        if (emailAddress != null && emailAddress.length() > 0) {
            buffer.append("\n\nOnce the report is generated,");
            buffer.append(" an email notification will be sent to");
            buffer.append(" " + emailAddress + ".");
        }
        return HTTPUtils.sessionMsg(request, buffer);
    }
}
