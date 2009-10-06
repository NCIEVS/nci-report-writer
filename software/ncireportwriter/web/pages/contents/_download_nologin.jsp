<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<%
  SDKClientUtil sdkclientutil = new SDKClientUtil();
  StandardReportTemplate standardReportTemplate = null;
  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
  Object[] objs = sdkclientutil.search(FQName);
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
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->
            <tr>
              <td valign="bottom" class="standardText">
                Reports with a DRAFT status are under review by subject experts; 
                others with an APPROVED status are ready for download. 
                Click on the report label to download the selected report.
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
                    <th class="dataTableHeader" scope="col" align="center">Format</th>
                    <th class="dataTableHeader" scope="col" align="center">Coding Scheme</th>
                    <th class="dataTableHeader" scope="col" align="center">Version</th>
                    <th class="dataTableHeader" scope="col" align="center">Created By</th>
                    <th class="dataTableHeader" scope="col" align="center">Last Modified</th>
                    <th class="dataTableHeader" scope="col" align="center">Status</th>
                  </tr>

                  <%
                    if (objs != null && objs.length > 0) {
                      for (int i=0; i<objs.length; i++) {
                        StandardReport standardReport = (StandardReport) objs[i];
                        ReportFormat reportFormat = standardReport.getFormat();
                        ReportStatus reportStatus = standardReport.getStatus();
                        standardReportTemplate = standardReport.getTemplate();
                        if (reportFormat == null || standardReportTemplate == null 
                            || reportStatus == null)
                          continue;
                        String label = standardReportTemplate.getLabel();
                        String format = reportFormat.getDescription();
                        String codingScheme = standardReportTemplate.getCodingSchemeName();
                        String version = standardReportTemplate.getCodingSchemeVersion();
                        Date lastModified = standardReport.getLastModified();
                        String status = reportStatus.getLabel();
                        String pathname = standardReport.getPathName();
                        String filename = DataUtils.getFileName(pathname);
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
                          <td class="dataCellText"><%=label%></td>
                        <% } %>
                          <td class="dataCellText"><%=filename%></td>
                          <td class="dataCellText"><%=format%></td>
                          <td class="dataCellText"><%=codingScheme%></td>
                          <td class="dataCellText"><%=version%></td>
                          <td class="dataCellText"><%=loginName%></td>
                          <td class="dataCellText"><%=date_str%></td>
                          <td class="dataCellText"><%=status%></td>
                        </tr>
                  <%
                      }
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