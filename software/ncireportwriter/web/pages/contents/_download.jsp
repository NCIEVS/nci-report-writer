<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<f:view>
  <h:form id="DOWNLOAD_REPORTForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0">
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">DOWNLOAD REPORT</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                      <%
                        String formatId = null;
                        String formatDescription = null;
                        String template = (String) request.getSession()
                          .getAttribute("selectedStandardReportTemplate");
                        String templateId = null;
                        try {
                          SDKClientUtil sdkclientutil = new SDKClientUtil();
                          String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
                          String methodName = "setLabel";
                          Object obj = sdkclientutil.search(FQName, methodName, template);
                          StandardReportTemplate standardReportTemplate = (StandardReportTemplate) obj;
                          templateId = (String) DataUtils.int2String(standardReportTemplate.getId());  
                        } catch (Exception ex) {
                        }
                        
                        List list = DataUtils.getAvailableReportFormat();
                        if (list != null && list.size() > 0)
                        {
                          for (int i=0; i<list.size(); i++)
                          {
                            ReportFormat reportFormat = (ReportFormat) list.get(i);
                            formatId = (String) DataUtils.int2String(reportFormat.getId());
                            formatDescription = reportFormat.getDescription();
                      %>
                            <tr class="dataRowLight">
                              <td class="dataCellText">
                                <a href="<%=request.getContextPath() %>/fileServlet?template=<%=templateId%>&format=<%=formatId%>" >
                                  <%=template%> (<%=formatDescription%>)
                                </a>
                              </td>
                            </tr>
                      <%          
                          }
                        }
                      %>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="back" value="Back" action="back" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table> <!-- Table 2 (End) -->
        </td>
      </tr>
    </table> <!-- Table 1 (End) -->    
  </h:form>
</f:view>
