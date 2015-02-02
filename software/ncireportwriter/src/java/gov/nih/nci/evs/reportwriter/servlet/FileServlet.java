/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.*;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.properties.*;

/**
 *
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public final class FileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger _logger = Logger.getLogger(FileServlet.class);

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */

    public void sendErrorResponse(HttpServletRequest request,
            HttpServletResponse response, String message) throws IOException,
            ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("message", message);

            String nextJSP = "/pages/message.jsf";
            RequestDispatcher dispatcher =
                getServletContext().getRequestDispatcher(nextJSP);
            dispatcher.forward(request, response);
        }
    }


    public void sendResponse(HttpServletRequest request, HttpServletResponse response, String fullPathName, String format)
            throws IOException, ServletException {
        response.setContentType("text/html");
        // String line_br = "\n";
        String line_br = "\r\n";

        response.setHeader("Cache-Control", "no-cache");

        if (format.indexOf("Excel") != -1) {
            String contentType = "application/vnd.ms-excel";
            response.setContentType(contentType);
            line_br = "";
        }

        File file = new File(fullPathName);
        String filename = file.getName();
System.out.println("sendResponse fullPathName: " + fullPathName);
System.out.println("sendResponse format: " + format);
System.out.println("sendResponse filename: " + filename);

        // HKR, GForge# 19467 - wrong placement of '\' in header.
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + filename + "\"");

        _logger.debug("Full path name =" + fullPathName);
        _logger.debug("File name =" + filename);

        if (format.indexOf("Excel") == -1) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(bis));

                String s = "";
                while (true) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    s = s + line + line_br;
                }
                PrintWriter out = response.getWriter();
                out.write(s);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (format.indexOf("Excel") != -1) {
            try {
                InputStream myxls = new FileInputStream(fullPathName);
                HSSFWorkbook wb = new HSSFWorkbook(myxls);
                try {
                    wb.write(response.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } catch (Exception e) {
                // LogUtils.log(logger, Level.ERROR, e);
                e.printStackTrace();
            }
        }
    }


    public void sendResponse(HttpServletRequest request, HttpServletResponse response,
            StandardReport standardReport) throws IOException, ServletException {
        // processing download
        String fullPathName = standardReport.getPathName();
        ReportFormat rf = standardReport.getFormat();
        String format = rf.getDescription();
        sendResponse(request, response, fullPathName, format);
    }


    public void sendResponse(HttpServletRequest request, HttpServletResponse response,
                             int formatId,
	                         int templateId) throws IOException, ServletException {
		String hibernate_cfg_xml = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/hibernate.cfg.xml");
		File f = new File(hibernate_cfg_xml);
		if (f.exists()) {
			JDBCUtil util = new JDBCUtil(hibernate_cfg_xml);
			Vector report_metadata_vec = util.getReportData();

			for (int i=0; i<report_metadata_vec.size(); i++) {
				ReportMetadata rmd = (ReportMetadata) report_metadata_vec.elementAt(i);
				int template_Id = rmd.getTemplateId();
				int format_Id = rmd.getFormatId();
				if (format_Id == formatId && template_Id == templateId) {
					String fullPathName = rmd.getPathName();
					if (fullPathName == null || fullPathName.compareTo("") == 0) {
						String templateLabel = rmd.getTemplateLabel();
						String version = rmd.getVersion();
						String download_dir = AppProperties.getInstance().getProperty(AppProperties.REPORT_DOWNLOAD_DIRECTORY);
						fullPathName = download_dir + File.separator + templateLabel + "__" + version + "."
						   + DataUtils.getFileExtension(rmd.getFormatId()) ;
						fullPathName = fullPathName.replaceAll(" ", "_");
					}
					String format = rmd.getFormat();
					sendResponse(request, response, fullPathName, format);
					return;
				}
			}
		} else {
			String message = "Report not found (format id: " + formatId + " templateId: " + templateId + ")";
			sendErrorResponse(request, response, message);
		}
	}


    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // <td class="dataCellText"><a
        // href="<%=request.getContextPath() %>/fileServlet?template=<%=templateId%>&format=<%=formatId%>"
        // ><%=template%> (<%=formatDescription%>)</a></td>

        // Get ReportFormat from database
        Vector report_metadata_vec = null;
        String action = request.getParameter("action");
        if (action != null && action.compareTo("download") == 0) {
			String hibernate_cfg_xml = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/hibernate.cfg.xml");
			//C:\Tomcat 7.0.54\webapps\ncireportwriter\WEB-INF\classes\hibernate.cfg.xml
			//hibernate_cfg_xml = hibernate_cfg_xml.replaceAll("\"", "/");
			File f = new File(hibernate_cfg_xml);
			if (f.exists()) {
				JDBCUtil util = new JDBCUtil(hibernate_cfg_xml);
				report_metadata_vec = util.getReportData();
				if (report_metadata_vec != null) {
					request.getSession().setAttribute("report_metadata_vec", report_metadata_vec);
				}
			}

			try {
				String nextJSP = "/pages/main/download_nologin.jsf";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				dispatcher.forward(request,response);

			} catch (Exception ex) {
				String message = "ERROR JDBCUtil - Unable to retrieve report data.";
				sendErrorResponse(request, response, message);
				ex.printStackTrace();
			}

			return;
		}

        String formatId = request.getParameter("format");
        String templateId = request.getParameter("template");

System.out.println("FileServlet formatId: " +  formatId);
System.out.println("FileServlet templateId: " +  templateId);

        sendResponse(request, response, Integer.parseInt(formatId), Integer.parseInt(templateId));


/*

        StandardReportTemplate standardReportTemplate = null;
        String message = "Format ID " + formatId + " not found.";
        try {
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            String methodName = "setId";
            int format_id = Integer.parseInt(formatId);
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            Object reportFormat_obj =
                sdkclientutil.search(FQName, methodName, format_id);
            if (reportFormat_obj == null) {


System.out.println("FileServlet unable to find ReportFormat with Id: " +  formatId);


                sendErrorResponse(request, response, message);
                return;
            }
            // Get StandardReportTemplate from database
            message = "Template ID " + templateId + " not found.";
            try {
                // Get all
                // gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate
                // Find ov.nih.nci.evs.reportwriter.bean.StandardReport with
                // matched template and format
                // Find the fullpath of the StandardReport
                // Check ReportStatus (send not available or pending approval
                // message as needed)

                FQName =
                    "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
                methodName = "setId";
                int template_id = Integer.parseInt(templateId);
                Object standardReportTemplate_obj =
                    sdkclientutil.search(FQName, methodName, template_id);
                if (standardReportTemplate_obj == null) {


System.out.println("FileServlet unable to find StandardReportTemplate with Id: " +  templateId);


                    sendErrorResponse(request, response, message);
                }

                standardReportTemplate =
                    (StandardReportTemplate) standardReportTemplate_obj;

                // Search for the matched StandardReport in the database (by
                // template and format)
                FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
                message = "The selected report is not available for download.";

                Object[] objs = sdkclientutil.search(FQName);

                if (objs == null || objs.length == 0) {


System.out.println("FileServlet unable to search for StandardReport");


                    sendErrorResponse(request, response, message);
                } else {
                    for (int i = 0; i < objs.length; i++) {
                        StandardReport standardReport =
                            (StandardReport) objs[i];


 System.out.println("FileServlet StandardReport " + i);



                        StandardReportTemplate srt =
                            standardReport.getTemplate();

  System.out.println("FileServlet StandardReportTemplate " + srt.getLabel());



                        int i1 = srt.getId().intValue();
                        int i2 = standardReportTemplate.getId().intValue();

                        if (i1 == i2) {
                            ReportFormat rf = standardReport.getFormat();
                            if (rf != null && rf.getId() == format_id) {
                                HttpSession session = request.getSession(false);
                                Boolean isAdmin =
                                    (Boolean) session.getAttribute("isAdmin");
                                if (isAdmin != null
                                        && isAdmin.equals(Boolean.TRUE)) {
                                    sendResponse(response, standardReport);
                                } else {
                                    // Check if the report has been approved:
                                    ReportStatus rs =
                                        standardReport.getStatus();
                                    if (rs != null) {
                                        String approved = "APPROVED";
                                        if (approved.equals(rs.getLabel())) {
                                            sendResponse(response,
                                                    standardReport);
                                        } else {
                                            sendErrorResponse(request,
                                                    response,
                                                    "The selected report has not yet been approved.");
                                        }
                                    } else {
                                        sendErrorResponse(request, response,
                                                "The selected report has not yet been approved.");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                sendErrorResponse(request, response, message);
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            sendErrorResponse(request, response, message);
            ex.printStackTrace();
        }
*/


    }
}
