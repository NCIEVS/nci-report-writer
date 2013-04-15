/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

import java.io.*;

import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class ReportDownloadRequest {
    private static Logger _logger =
        Logger.getLogger(ReportDownloadRequest.class);

    public String downloadReportAction(String selectedStandardReportTemplate) {
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
                selectedStandardReportTemplate);

        UserSessionBean usBean = BeanUtils.getUserSessionBean();
        usBean
                .getStandardReportTemplate(selectedStandardReportTemplate);
        // String ontologyNameAndVersion =
        // standardReportTemplate.getCodingSchemeName() + " (version: " +
        // standardReportTemplate.getCodingSchemeVersion() + ")";

        _logger.debug("downloading report " + selectedStandardReportTemplate);

        String download_dir = null;
        try {
            download_dir =
                AppProperties.getInstance().getProperty(
                        AppProperties.REPORT_DOWNLOAD_DIRECTORY);
            // logger.debug("download_dir " + download_dir);

        } catch (Exception ex) {
            return HTTPUtils.sessionMsg(request,
                    "Unable to download the specified report.\n"
                            + "Download directory does not exist.\n"
                            + "Check with your system administrator.");
        }

        File dir = new File(download_dir);
        if (!dir.exists()) {
            _logger.debug("Unable to download the specified report "
                    + "-- download directory does not exist. ");
            return HTTPUtils.sessionMsg(request, "Unable to download "
                    + selectedStandardReportTemplate + ".\n"
                    + "Download directory does not exist.");
        }

        File[] fileList = dir.listFiles();
        int len = fileList.length;
        while (len > 0) {
            len--;
            if (!fileList[len].isDirectory()) {
                String name = fileList[len].getName();
                _logger.debug("File found in the download directory: " + name);
            }
        }

        boolean approved = true;
        if (approved)
            return "download";

        return HTTPUtils.sessionMsg(request, "The "
                + selectedStandardReportTemplate
                + " has not been approved for download.");
    }
}
