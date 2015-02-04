<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.service.*" %>
<%@ page import="gov.nih.nci.evs.utils.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.properties.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
  private static final String buildInfo = AppProperties.getInstance().
    getSpecialProperty(AppProperties.BUILD_INFO);
  private static final String application_version = AppProperties.getInstance().
    getSpecialProperty(AppProperties.APPLICATION_VERSION);
  private static final String anthill_build_tag_built = AppProperties.getInstance().
    getSpecialProperty(AppProperties.ANTHILL_BUILD_TAG_BUILT);
  private static final String evs_service_url = AppProperties.getInstance().
    getSpecialProperty(AppProperties.EVS_SERVICE_URL);
%>

<%


String download_dir = AppProperties.getInstance().getProperty(AppProperties.REPORT_DOWNLOAD_DIRECTORY);
System.out.println("(*) DOWNLOAD DIR: " + download_dir);
SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

boolean approvedOnly = true;

String hibernate_cfg_xml = request.getSession().getServletContext().getRealPath(JDBCUtil.HIBERNATE_CFG_PATH);//"/WEB-INF/classes/hibernate.cfg.xml");
System.out.println("hibernate_cfg_xml: " + hibernate_cfg_xml);
File file = new File(hibernate_cfg_xml);
if (file.exists()) {
    System.out.println("Note: hibernate_cfg_xml exists.");
    JDBCUtil util = new JDBCUtil(hibernate_cfg_xml);
    System.out.println("username: " + util.getUsername());
    System.out.println("password: " + util.getPassword());
    System.out.println("url: " + util.getUrl());
    System.out.println("driver: " + util.getDriver());
    System.out.println("configuration_file: " + util.getConfigurationFile());
    
} else {
    System.out.println("WARNING: Unable to access hibernate_cfg_xml -- file.exists() method failed.");
}
		
  
  String label = null;
  int templateId = -1;
  String templateId_str = null;
  String filename = null;
  String filesize = null;
  int formatId = -1;
  String format = null;
  String codingScheme = null;
  String version = null;
  String loginName = null;
  String date_str = null;
  String status = null;
  String pathName = null;
  String dataRowColor = null;
  String formatId_str = null;
  String labelUrl = null;

  Vector report_metadata_vec = (Vector) request.getSession().getAttribute("report_metadata_vec");
  if (report_metadata_vec == null) {
      System.out.println("(*) WARNING: report_metadata_vec is NULL???");
      System.out.println("(*) Trying to call util.getReportData() again...");
      report_metadata_vec = util.getReportData();
      if (report_metadata_vec == null) {
           System.out.println("(*) WARNING: report_metadata_vec is still NULL???");
      }
      
  } else { 
      System.out.println("(*) report_metadata_vec.size() = " + report_metadata_vec.size());
  }
  
  String imagesPath = FormUtils.getImagesPath(request);
  String title = "NCI Report Writer: Download";

   
%>
<!--
   Build info: <%=buildInfo%>
 Version info: <%=application_version%>
          Tag: <%=anthill_build_tag_built%>
   LexEVS URL: <%=evs_service_url%>          
  -->
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><%=title%></title>
    <link rel="stylesheet" type="text/css" href="<%= FormUtils.getCSSPath(request) %>/styleSheet.css" />
    <link rel="shortcut icon" href="<%= FormUtils.getBasePath(request) %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  </head>
  <body>
    <%@ include file="/pages/templates/header.jsp"%>
    <div class="center-page">
     <%@ include file="/pages/templates/sub_header.jsp"%>
     <div class="mainbox-top"><img src="<%=imagesPath%>/mainbox-top.gif"
        width="947" height="5" alt="Mainbox Top" /></div>
      <div id="main-area">
        <%@ include file="/pages/templates/application_banner.jsp"%>
        <%@ include file="/pages/templates/quick_links.jsp"%>

        <div class="pagecontent">
          <%@ include file="/pages/contents/menu_bar.jsp"%>


<f:view>
  <h:form id="AVAILABLE_STANDARD_REPORTSForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
      width="100%" height="100%"> <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPageWithoutPadding"> <!-- Table 2 (Begin) -->
            <tr>
              <td valign="bottom" class="standardText">
                Reports with a DRAFT status are under review by subject experts;<br/>
                others with an APPROVED status are ready for download.<br/>
                Click on the report label to download the selected report.<br/>
                <br/>
              </td>
            </tr>

            <tr>
              <td class="dataTablePrimaryLabel" height="20">REPORTS</td>
            </tr>
            
            <tr>
              <td>
                <table summary="Enter summary of data here" 
                    cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
                  <tr>
                    <th class="dataTableHeader" scope="col" align="center">Report Label</th>
                    <th class="dataTableHeader" scope="col" align="center">File Name</th>
                    <th class="dataTableHeader" scope="col" align="center">File Size</th>
                    <th class="dataTableHeader" scope="col" align="center">Format</th>
                    <th class="dataTableHeader" scope="col" align="center">Coding Scheme</th>
                    <th class="dataTableHeader" scope="col" align="center">Version</th>
                    <th class="dataTableHeader" scope="col" align="center">Created By</th>
                    <th class="dataTableHeader" scope="col" align="center">Last Modified</th>
                    <th class="dataTableHeader" scope="col" align="center">Status</th>
                  </tr>

                  <%
		int lcv = 0;
		if (report_metadata_vec != null) {
		for (int i=0; i<report_metadata_vec.size(); i++) {
			ReportMetadata rmd = (ReportMetadata) report_metadata_vec.elementAt(i);
			label = rmd.getTemplateLabel();
			templateId = rmd.getTemplateId();
			templateId_str = new Integer(templateId).toString();
			filename = rmd.getLabel();
			filesize = "";
			date_str = rmd.getLastModified();

			pathName = rmd.getPathName();
			if (pathName == null) {
			    pathName = download_dir + File.separator + filename;
			}
			File f = new File(pathName);
			if (f.exists()) {
			    filesize = DataUtils.getFileSize(pathName);
			    if (date_str == null) {
				date_str = DataUtils.getLastModified(f);
			    }
			} 
			format = rmd.getFormat();
			codingScheme = rmd.getCodingScheme();
			version = rmd.getVersion();
			loginName = rmd.getCreatedBy();

			status = rmd.getStatus();	
			formatId = rmd.getFormatId();
			formatId_str = new Integer(formatId).toString();
				
			dataRowColor = lcv%2==0 ? "dataRowLight" : "dataRowDark";
			lcv++;

			labelUrl = null;
			if (!approvedOnly || (approvedOnly && status.compareToIgnoreCase("APPROVED") == 0)) {
			    labelUrl = request.getContextPath() + "/fileServlet?template=" + templateId_str + "&format=" + formatId_str;
			}
			
		  %>

			      <tr class="<%=dataRowColor%>">
			      <% if (labelUrl != null) { %>
				<td class="dataCellText"><a href="<%=labelUrl%>"><%=label%></a></td>
			      <% } else { %>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(label)%></td>
			      <% } %>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(filename)%></td>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(filesize)%></td>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(format)%></td>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(codingScheme)%></td>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(version)%></td>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(loginName)%></td>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(date_str)%></td>
				<td class="dataCellText"><%=StringUtils.getSpaceIfBlank(status)%></td>
			      </tr>
	 	 <%
                 } // for loop
                 }
                 %>
                </table>
              </td>
            </tr>
          </table> <!-- Table 2 (End) -->
        </td>
      </tr>
    </table> <!-- Table 1 (End) -->
  </h:form>
</f:view> 

          
          <%@ include file="/pages/templates/footer.jsp"%>
        </div>
      </div>
      <!-- Note: Keep the div below on one line to avoid IE bug. -->
      <div class="mainbox-bottom"><img src="<%=imagesPath%>/mainbox-bottom.gif"
        width="947" height="5" alt="Mainbox Bottom" /></div>
    </div>
    <br/>
  </body>
</html>