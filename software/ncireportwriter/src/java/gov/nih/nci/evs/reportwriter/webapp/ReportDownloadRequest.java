package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

import java.io.*;

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
