<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<f:view>
<h:form id="AVAILABLE_STANDARD_REPORTSForm">
    <tr>
        <td width="100%" valign="top"><br>
        <!-- target of anchor to skip menus --><a name="content" />
            <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
                <tr>
                    <td>
                        <table summary="" cellpadding="0" cellspacing="0" border="0">
                                <tr><td height="40" valign="bottom" class="standardText">Reports with a DRAFT status are under review by subject experts; others with an APPROVED status are ready for download. Click on the report label to download the selected report.</td></tr>

                            <tr>
                                <td class="dataTablePrimaryLabel" height="20">REPORTS</td>
                            </tr>
                            <tr>
                                <td>
                                    <table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
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
        try{
            SDKClientUtil sdkclientutil = new SDKClientUtil();
        StandardReportTemplate standardReportTemplate = null;
        String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
        Object[] objs = sdkclientutil.search(FQName);
        int n = 0;
        if (objs != null && objs.length > 0) {
            for (int i=0; i<objs.length; i++)
            {
                StandardReport standardReport = (StandardReport) objs[i];
                ReportFormat reportFormat = standardReport.getFormat();
                ReportStatus reportStatus = standardReport.getStatus();
                standardReportTemplate = standardReport.getTemplate();
                if (reportFormat != null && standardReportTemplate != null && reportStatus != null)
                {
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
                  
                  String date_str = null;
                  if (lastModified != null)
                  {
                      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                      date_str = formatter.format(lastModified);
                  }
                  if (n % 2 == 0)
                  {
                  %>
                      <tr class="dataRowLight">
                  <%    
                  }
                  else
                  {
                  %>
                      <tr class="dataRowDark"> 
                  <%    
                  }
                  
                  Integer id = standardReportTemplate.getId();
                  String templateId = id.toString();
                  
                  Integer format_id = reportFormat.getId();
                  String formatId = format_id.toString();


%>

<%
if (status.compareToIgnoreCase("APPROVED") == 0)
{
%>
<td class="dataCellText"><a href="<%=request.getContextPath() %>/fileServlet?template=<%=templateId%>&format=<%=formatId%>" ><%=label%></a></td>
<%
}
else
{
%>
<td class="dataCellText"><%=label%></td>
<%
}
%>

                                        
                                            <td class="dataCellText"><%=filename%></td>
                                            <td class="dataCellText"><%=format%></td>
                                            <td class="dataCellText"><%=codingScheme%></td>
                                            <td class="dataCellText"><%=version%></td>
                                            <td class="dataCellText"><%=loginName%></td>
                                            <td class="dataCellText"><%=date_str%></td>
                                            <td class="dataCellText"><%=status%></td>
                                        </tr>
                                        
                                        
<%
                                  n++; 
                }
           }
        }
        } catch(Exception e) {
            e.printStackTrace();
        }

%>


                        
                                        
                                        
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</h:form>
</f:view>