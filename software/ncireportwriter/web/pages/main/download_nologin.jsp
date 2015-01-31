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
          


<%
  SDKClientUtil sdkclientutil = new SDKClientUtil();
  StandardReportTemplate standardReportTemplate = null;
  //String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
  //String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
  
  //Object[] objs = sdkclientutil.search(FQName);
  //Vector<StandardReport> vector = ListConverter.toStandardReport(objs, true);
  
String download_dir = AppProperties.getInstance().getProperty(AppProperties.REPORT_DOWNLOAD_DIRECTORY);
System.out.println("(*) DOWNLOAD DIR: " + download_dir);
  
  SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
%>

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
                    int i = 0;
                    ReportFormat reportFormat = null;
                    ReportStatus reportStatus = null;
                    String label = null;
                    String format = null;
                    String codingScheme = null;
                    String version = null;
                    Date lastModified = null;
                    String status = null;
                    String pathname = null;
                    String filename = null;

                    gov.nih.nci.evs.reportwriter.bean.User user = null;
                    String loginName = null;
                    String date_str = null;
                    String dataRowColor = null;
                      
                    Integer id = null;
                    String templateId = null;
                    Integer format_id = null;
                    String formatId = null;
                    String labelUrl = null;
                    String filesize = "";
 
		    StandardReportServiceProvider provider = new StandardReportServiceProvider();
		    List label_list = provider.getStandardReportTemplateLabels();
		    standardReportTemplate = null;
		    int lcv = 0;
		    for (i=0; i<label_list.size(); i++) {
			label = (String) label_list.get(i);
			try {
			    standardReportTemplate = provider.getStandardReportTemplate(label);
			    //templateId = standardReportTemplate.getId().toString();
			    //label = standardReportTemplate.getLabel();
			    codingScheme = standardReportTemplate.getCodingSchemeName();
			    version = standardReportTemplate.getCodingSchemeVersion();
			    Collection<StandardReport> c = standardReportTemplate.getReportCollection();
			    //System.out.println("StandardReport: " + c.size());
			    List list = new ArrayList(c);
			    for (int k=0; k<list.size(); k++) {
				StandardReport standardReport = (StandardReport) list.get(k);
				filename = standardReport.getLabel();
				//pathname = standardReport.getPathName();
				lastModified = standardReport.getLastModified();
				format = DataUtils.getFileFormat(filename);
				pathname = download_dir + File.separator + filename;
				filesize = "";
				date_str = null;
				
System.out.println(pathname);				
				
				
				File f = new File(pathname);
				if (f.exists()) {
				    filesize = DataUtils.getFileSize(pathname);
				    //if (lastModified == null) {
				    date_str = DataUtils.getLastModified(f);
				    //} 
				}
				if (lastModified != null && date_str == null) {
				    date_str = formatter.format(lastModified);
				} 
				
				if (date_str == null) date_str = "";

				//user = standardReport.getCreatedBy();
				//loginName = user.getLoginName();
				
				user = null;
				try {
				    user = standardReport.getCreatedBy();
				    if (user != null) {
				        loginName = user.getLoginName();
				    }
				} catch (Exception ex) {
				    loginName = "";
				}
				
				reportStatus = null;
				try {
				    reportStatus = standardReport.getStatus();
				    if (reportStatus != null) {
				        status = reportStatus.getLabel();
				    }
				} catch (Exception ex) {
				    status = "";
				}
				
				
				dataRowColor = lcv%2==0 ? "dataRowLight" : "dataRowDark";
				lcv++;
				id = standardReportTemplate.getId();
				templateId = id.toString();
				format_id = DataUtils.getFormatId(filename);
				formatId = format_id.toString();
				labelUrl = null;
			            //if (status.compareToIgnoreCase("APPROVED") == 0) {
				labelUrl = request.getContextPath() + "/fileServlet?template=" + templateId + "&format=" + formatId;
				
			            //}
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
			      
			    } //for loop
			 } catch (Exception ex) {
			 
			 }
                     } // for loop
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