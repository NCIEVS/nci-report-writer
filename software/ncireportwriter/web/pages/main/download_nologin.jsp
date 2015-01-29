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
          
          
<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%
  SDKClientUtil sdkclientutil = new SDKClientUtil();
  StandardReportTemplate standardReportTemplate = null;
  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
  Object[] objs = sdkclientutil.search(FQName);
  Vector<StandardReport> vector = ListConverter.toStandardReport(objs, true);
  
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
                  
                    int i=0;
                    ReportFormat reportFormat = null;
                    ReportStatus reportStatus = null;
                    String label = null;
                    //Iterator<StandardReport> iterator = vector.iterator();
                    //while (iterator.hasNext()) {
                      //StandardReport standardReport = iterator.next();
                      
                    for (int k=0; k<vector.size(); k++) {
                      StandardReport standardReport = (StandardReport) vector.elementAt(k); 
                      
                      if (standardReport == null) {
                          System.out.println("standardReport == null???");
                      } else {
                          System.out.println("standardReport != null");
                          try {                          
				  standardReportTemplate = standardReport.getTemplate();
				  label = standardReportTemplate.getLabel();
				  reportStatus = standardReport.getStatus();
				  reportFormat = standardReport.getFormat();
			  } catch (Exception ex) {
			       ex.printStackTrace();
			  }
		      }
                      
                      if (reportFormat == null || standardReportTemplate == null 
                          || reportStatus == null) {
                          continue;
                      }
                        
                     
                      String format = reportFormat.getDescription();
                                            
                      String codingScheme = standardReportTemplate.getCodingSchemeName();
                      String version = standardReportTemplate.getCodingSchemeVersion();
                      Date lastModified = standardReport.getLastModified();
                      String status = reportStatus.getLabel();
                      String pathname = standardReport.getPathName();
                      String filename = DataUtils.getFileName(pathname);
                      
                      String filesize = "";
                      if (pathname == null || pathname.compareTo("") == 0) {
                          pathname = download_dir + File.separator + label + DataUtils.getFileExtension(format);
                      }
                      
                      //System.out.println("(*) PATHNAME: " + pathname);
                      
                      filesize = DataUtils.getFileSize(pathname);
                      
                      gov.nih.nci.evs.reportwriter.bean.User user = standardReport.getCreatedBy();
                      String loginName = user.getLoginName();
                      String date_str = lastModified == null ? null : formatter.format(lastModified);
                      String dataRowColor = i%2==0 ? "dataRowLight" : "dataRowDark";
                      Integer id = standardReportTemplate.getId();
                      String templateId = id.toString();
                      Integer format_id = reportFormat.getId();
                      String formatId = format_id.toString();
                      String labelUrl = null;
                      if (status.compareToIgnoreCase("APPROVED") == 0)
                        labelUrl = request.getContextPath() + "/fileServlet?template=" +
                          templateId + "&format=" + formatId;
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
                      ++i;
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