/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.service;

import java.io.*;
import java.util.*;
import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class StandardReportService {
    private static Logger _logger =
        Logger.getLogger(StandardReportService.class);

    public static PrintWriter openPrintWriter(String outputfile) {
        try {
            PrintWriter pw =
                new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));
            return pw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closePrintWriter(PrintWriter pw) {
        if (pw == null) {
            _logger.debug("PrintWriter is not open.");
            return;
        }
        pw.close();
        pw = null;
    }

    public static Boolean generateStandardReport(String outputDir,
        String standardReportLabel, String uid, String emailAddress) {
        Thread reportgeneration_thread =
            new Thread(new ReportGenerationThread(outputDir,
                standardReportLabel, uid, emailAddress));
        reportgeneration_thread.start();

        // to be modified
        return Boolean.TRUE;
    }

    public static String validReport(String standardReportTemplate_value,
        String reportFormat_value, String reportStatus_value, String user_value) {

        try {
            initializeReportFormats();
            initializeReportStatus();

            if (standardReportTemplate_value == null)
                return "Report template not specified.";
            if (reportFormat_value == null)
                return "Report format not specified.";
            if (reportStatus_value == null)
                return "Report status not specified.";
            if (user_value == null)
                return "User authentication failed.";

            SDKClientUtil sdkclientutil = new SDKClientUtil();

            String FQName = null;
            String methodName = null;
            String key = null;

            FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            methodName = "setLabel";
            key = standardReportTemplate_value;
            Object standardReportTemplate_obj =
                sdkclientutil.search(FQName, methodName, key);
            if (standardReportTemplate_obj == null) {
                _logger.error("Object " + standardReportTemplate_value
                    + " not found.");
                return "Unidentifiable report template label -- "
                    + standardReportTemplate_value;
            }

            FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            methodName = "setDescription";
            key = reportFormat_value;
            Object reportFormat_obj =
                sdkclientutil.search(FQName, methodName, key);
            if (reportFormat_obj == null) {
                _logger.error("Object " + reportFormat_value + " not found.");
                return "Unidentifiable report format -- " + reportFormat_value;
            }

            FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
            methodName = "setLabel";
            key = reportStatus_value;
            Object reportStatus_obj =
                sdkclientutil.search(FQName, methodName, key);
            if (reportStatus_obj == null) {
                _logger.error("Object " + reportStatus_value + " not found.");
                return "Unidentifiable report status -- " + reportStatus_value;
            }

            FQName = "gov.nih.nci.evs.reportwriter.bean.User";
            methodName = "setLoginName";
            key = user_value;
            Object user_obj = sdkclientutil.search(FQName, methodName, key);
            if (user_obj == null) {
                _logger.error("Object " + user_value + " not found.");
                return "Unidentifiable user -- " + user_value;
            }

            return "success";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Report validation falied";
    }

    /**
     * Method called upon generation of two reports (tab-delimited and Excel).
     */
    public static Boolean createStandardReport(String label, String pathName,
        String templateLabel, String format, String status, String uid) {
        try {

            _logger.debug("Validing report ");

            String msg = validReport(templateLabel, format, status, uid);
            if (msg.compareTo("success") != 0) {
                _logger.error("Report object not created.");
                return Boolean.FALSE;
            }

            SDKClientUtil sdkclientutil = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            String methodName = "setLabel";
            String key = label;
            Object[] objs = sdkclientutil.search(FQName);

            _logger.debug("Deleting old report objects from database. ");

            if (objs != null) {
                // report already exists, delete it
                for (int i = 0; i < objs.length; i++) {
                    StandardReport report = (StandardReport) objs[i];
                    String reportlabel = report.getLabel();
                    if (label.compareTo(reportlabel) == 0) {
                        sdkclientutil.deleteStandardReport(report);
                    }
                }
            }

            _logger.debug("Creating StandardReport object. ");

            java.util.Date lastModified = new Date(); // system date
            StandardReport report =
                sdkclientutil.createStandardReport(label, lastModified,
                    pathName);

            FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            methodName = "setLabel";

            _logger.debug("Report template: " + label);
            key = templateLabel;

            Object template_obj = sdkclientutil.search(FQName, methodName, key);
            if (template_obj != null) {
                report.setTemplate((StandardReportTemplate) template_obj);
            } else {
                _logger.error("Report template: " + label + " not found???");
                return Boolean.FALSE;
            }

            _logger.debug("Assigning report format. ");

            FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            methodName = "setDescription";
            key = format;

            Object format_obj = sdkclientutil.search(FQName, methodName, key);
            if (format_obj != null) {
                report.setFormat((ReportFormat) format_obj);
            } else {
                _logger.error("Format " + format
                    + " not found -- report not created.");
                return Boolean.FALSE;
            }

            _logger.debug("Assigning report status. ");

            FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
            methodName = "setLabel";
            key = status;

            Object status_obj = sdkclientutil.search(FQName, methodName, key);
            if (status_obj != null) {
                report.setStatus((ReportStatus) status_obj);
            }

            _logger.debug("Assigning user. ");

            FQName = "gov.nih.nci.evs.reportwriter.bean.User";
            methodName = "setLoginName";
            key = uid;

            Object user_obj = sdkclientutil.search(FQName, methodName, key);
            if (user_obj != null) {
                report
                    .setCreatedBy((gov.nih.nci.evs.reportwriter.bean.User) user_obj);
            }

            _logger.debug("Writing record to database. ");
            sdkclientutil.insertStandardReport(report);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private static Boolean initializeReportStatus() {
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
            Object[] objs = util.search(FQName);

            if (objs == null || objs.length == 0) {

                // Initial status (DRAFT and APPROVED)
                String label = "DRAFT";
                String description =
                    "Report is a draft, not ready for download.";
                boolean active = true;
                try {
                    util.insertReportStatus(label, description, active);
                } catch (Exception ex) {
                    _logger.error("*** insertReportStatus DRAFT failed.");
                }

                label = "APPROVED";
                description = "Report has been approved for download by users";
                active = true;
                try {
                    util.insertReportStatus(label, description, active);
                } catch (Exception ex) {
                    _logger.error("*** insertReportStatus APPROVED failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private static Boolean initializeReportFormats() {
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            Object[] objs = util.search(FQName);

            if (objs == null || objs.length == 0) {
                String description = "Text (tab delimited)";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    _logger.error("*** insertReportFormat " + description
                        + " failed.");
                }

                description = "Microsoft Office Excel";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    _logger.error("*** insertReportFormat " + description
                        + " failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static void main(String[] args) {
        try {
            String standardReportLabel = "FDA-UNII Subset Report";
            String outputfile = standardReportLabel;
            outputfile.replaceAll(" ", "_");
            String outputDir = "G:\\ReportWriter\\test";
            StandardReportService.generateStandardReport(outputDir,
                standardReportLabel, "kimong", "Kim.Ong@ngc.com");

        } catch (Exception e) {
            _logger.error("REQUEST FAILED !!!");
        }
    }
}
