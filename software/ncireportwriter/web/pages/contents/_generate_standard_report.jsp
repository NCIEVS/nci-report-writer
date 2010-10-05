<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.properties.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%@ page import="gov.nih.nci.evs.utils.*" %>

<%
  String ncitUrl = AppProperties.getInstance().getProperty(
      AppProperties.NCIT_URL);
  String template_label = (String) request.getSession().
    getAttribute("selectedStandardReportTemplate");

  String templateLabel = (String) request.getSession().getAttribute("selectedStandardReportTemplate");
  SDKClientUtil sdkclientutil = new SDKClientUtil();
  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
  String methodName = "setLabel";
  Object obj = sdkclientutil.search(FQName, methodName, templateLabel);
  StandardReportTemplate standardReportTemplate = (StandardReportTemplate) obj;
  Collection cc = standardReportTemplate.getColumnCollection();
  Object[] objs = cc == null ? null : cc.toArray();
  
  String warningMsg = (String) request.getAttribute("warningMsg");
%>

<f:view>
  <h:form id="GENERATE_REPORTForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->  
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->  
            <% if (warningMsg != null && warningMsg.trim().length() > 0) { %>
              <tr><td class="warningMsgColor">
                Warning:<br/>
                <%=StringUtils.toHtml(warningMsg)%><br/>
                <br/>
              </td></tr>
            <% } %>              
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0"> <!-- Table 3 (Begin) -->  
                <tr><td>
                  Please select the following report columns that contain NCI Thesaurus (NCIt) codes.<br/>
                  In some of the generated report types, this will allow NCI Report Writer to make the<br/>
                  NCIt codes hyper-linkable to:
                  <ul><li>
                    <a href="<%=ncitUrl%>" target="_blank"><%=ncitUrl%></a>
                  </li></ul><br/>
                </td></tr>
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">GENERATE REPORT: <%=templateLabel%></td>
                  </tr>
                  <tr>
                    <td>
                      <table cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                        <tr>
                          <th class="dataTableHeader">&nbsp;</th>
                          <th class="dataTableHeader">Col. #</th>
                          <th class="dataTableHeader">Field Label</th>
                          <th class="dataTableHeader">Field Type</th>
                          <th class="dataTableHeader">Property Type</th>
                          <th class="dataTableHeader">Property Name</th>
                        </tr>
                        <%
                          for (int i=0; i<objs.length; i++) {
                            ReportColumn c = (ReportColumn) objs[i];
                        %>
                            <tr class="dataRowLight">
                              <td class="dataCellText"><input type="checkbox" name="selectedColumns" value="<%=i%>"></td>
                              <td class="dataCellNumerical"><%= c.getColumnNumber() %></td>
                              <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getLabel()) %></td>
                              <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getFieldId()) %></td>
                              <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getPropertyType()) %></td>
                              <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getPropertyName()) %></td>
                            </tr>
                        <%
                          }
                        %>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="generate" value="Generate" 
                              action="#{userSessionBean.generateStandardReportAction}" /></td>
                          <td><h:commandButton id="back" value="Back"
                              action="#{userSessionBean.displayStandardReportTemplateAction}" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table> <!-- Table 3 (End) -->  
              </td>
            </tr>
          </table> <!-- Table 2 (End) -->  
        </td>
      </tr>
    </table> <!-- Table 1 (End) -->  
  </h:form>
</f:view>