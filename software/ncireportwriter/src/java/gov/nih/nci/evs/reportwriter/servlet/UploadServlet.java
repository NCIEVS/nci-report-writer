package gov.nih.nci.evs.reportwriter.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.service.*;


import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history
 *     Initial implementation kim.ong@ngc.com
 *
 */

public final class UploadServlet extends HttpServlet {
    private static Logger _logger = Logger.getLogger(UploadServlet.class);
    /**
     * local constants
     */
    private static final long serialVersionUID = 1L;

    /**
     * Validates the Init and Context parameters, configures authentication URL
     *
     * @throws ServletException if the init parameters are invalid or any other
     *         problems occur during initialisation
     */
    public void init() throws ServletException {

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
     * @exception ServletException if a Servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }


	public String convertStreamToString(InputStream is, long size) throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[(int)size];
			try {
				Reader reader = new BufferedReader(
				new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}

			} finally {
				is.close();
			}
			return writer.toString();

		} else {
			return "";
		}

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

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Determine request by attributes
        String action = (String) request.getParameter("action");
        //String type = (String) request.getParameter("type");


		System.out.println("(*) UploadServlet ...action " + action);
        if (action == null) {
			action = "upload_data";
		}

		DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
		fileItemFactory.setSizeThreshold(1*1024*1024); //1 MB

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		System.out.println("action: " + action);

		String outputfile = null;

		try {
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while(itr.hasNext()) {
				FileItem item = (FileItem) itr.next();

				if(item.isFormField()) {
					System.out.println("File Name = "+item.getFieldName()+", Value = "+item.getString());

				} else {
					System.out.println("Field Name = "+item.getFieldName()+
						", File Name = "+item.getName()+
						", Content type = "+item.getContentType()+
						", File Size = "+item.getSize());

					//String s = convertStreamToString(item.getInputStream(), item.getSize());

String download_dir = AppProperties.getInstance().getProperty(AppProperties.REPORT_DOWNLOAD_DIRECTORY);
String filename = item.getName();//download_dir + File.separator + item.getName();
System.out.println("(*) filename: " + filename);

File f = new File(item.getName());
if (f.exists()) {
                    if (action.compareTo("hierarchy")== 0) {
						outputfile = HierarchyReportGenerator.getDefaultHierarchyReportName(filename);
						HierarchyReportGenerator.generateHierarchyReport(filename, outputfile, null);

					} else {
						ExcelMetadataUtils.dumpMetadata(filename);
					}
} else {
	System.out.println("(*) Unable to find the file: " + filename );
}


					/*
					request.getSession().setAttribute("action", action);

					if (action.compareTo("upload_data") == 0) {
						request.getSession().setAttribute("narrative", s);
					}
					*/

				}
			}
		}catch(FileUploadException ex) {
			log("Error encountered while parsing the request",ex);
		} catch(Exception ex) {
			log("Error encountered while uploading file",ex);
		}

// ADD SESSION VARIABLE MESSAGE:
		long ms = System.currentTimeMillis();
		if (action.compareTo("hierarchy") == 0) {
			//response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/pages/main/generate_hierarchy_report.jsf"));
			export_text_file(request, response, outputfile);

		} else {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/pages/main/enter_excel_metadata.jsf"));
		}
    }

    public void export_text_file(HttpServletRequest request, HttpServletResponse response, String filename) {
        StringBuffer sb = new StringBuffer();
        try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = br.readLine()) != null) {
			   sb.append(line);
			   sb.append("\r\n");
			}
			br.close();
		} catch (Exception ex)	{
			sb.append("WARNING: Hierarchy report generation failed.");
			ex.printStackTrace();
		}

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);

		response.setContentLength(sb.length());

		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			sb.append("WARNING: Hierarchy report generation failed.");
		}
		FacesContext.getCurrentInstance().responseComplete();
		return;
	}


}
