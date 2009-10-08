<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<%
  String template_label = (String) request.getSession().
    getAttribute("selectedStandardReportTemplate");
  String warningMsg = (String) request.getAttribute("warningMsg");
  
  String templatLabel = (String) request.getSession().getAttribute("selectedStandardReportTemplate");
  SDKClientUtil sdkclientutil = new SDKClientUtil();
  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
  String methodName = "setLabel";
  Object obj = sdkclientutil.search(FQName, methodName, templatLabel);
  StandardReportTemplate standardReportTemplate = (StandardReportTemplate) obj;
  Collection cc = standardReportTemplate.getColumnCollection();
  Object[] objs = cc == null ? null : cc.toArray();
%>
<f:view>
  <h:form id="STANDARD_REPORT_FIELDSForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->
            <% if (request.getAttribute("warningMsg") != null) { %>
              <tr><td class="warningMsgColor">
                Warning:<br/>
                <%=StringUtils.toHtml(warningMsg)%><br/>
                <br/>
              </td></tr>
            <% } %>
            <tr>
              <!--
              <td class="dataTablePrimaryLabel" height="20">REPORT FIELDS</td>
              --> 
              <td class="dataTablePrimaryLabel" height="20">
                <%= template_label %>
              </td>
            </tr>
            <tr>
              <td>
                <table summary="Enter summary of data here" 
                    cellpadding="2" cellspacing="0" border="0" 
                    class="dataTable" width="100%"> <!-- Table 4 (Begin) -->
                  <tr>
                    <th class="dataTableHeader" scope="col" align="center">Data Field</th>
                    <th class="dataTableHeader" scope="col" align="center">Col. #</th>
                    <th class="dataTableHeader" scope="col" align="center">Field #</th>
                    <th class="dataTableHeader" scope="col" align="center">Field Label</th>
                    <th class="dataTableHeader" scope="col" align="center">Field Type</th>
                    <th class="dataTableHeader" scope="col" align="center">Property Type</th>
                    <th class="dataTableHeader" scope="col" align="center">Property Name</th>
                    <th class="dataTableHeader" scope="col" align="center">Is Preferred?</th>
                    <th class="dataTableHeader" scope="col" align="center">Represent- ational Form</th>
                    <th class="dataTableHeader" scope="col" align="center">Source</th>
                    <th class="dataTableHeader" scope="col" align="center">Qualifier Name</th>
                    <th class="dataTableHeader" scope="col" align="center">Qualifier Value</th>
                    <th class="dataTableHeader" scope="col" align="center">Delimiter</th>
                    <th class="dataTableHeader" scope="col" align="center">Depend- ency</th>
                  </tr>
                  <% 
                    if (objs != null) {
                      for (int i=0; i<objs.length; i++) {
                        ReportColumn c = (ReportColumn) objs[i];
                  %>
                        <tr>
                          <td class="dataCellText"> <input type="radio" name="selectedColumnInfo" 
                              value="<%=c.getColumnNumber()%>:<%=c.getId()%>"></td>
                          <td class="dataCellNumerical"><%= c.getColumnNumber() %></td>
                          <td class="dataCellNumerical"><%= c.getId() %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getLabel()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getFieldId()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getPropertyType()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getPropertyName()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getIsPreferred()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getRepresentationalForm()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getSource()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getQualifierName()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getQualifierValue()) %></td>
                          <td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getDelimiter()) %></td>
                          <td class="dataCellNumerical"><%= c.getConditionalColumnId() %></td>
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
                  <% if (objs.length == -1) { %>
                    <td>
                      <h:commandButton id="insertbefore" value="InsertBefore" 
                          action="#{userSessionBean.insertBeforeColumnAction}" />
                    </td>
                    <td>
                      <h:commandButton id="insertafter"  value="InsertAfter"
                          action="#{userSessionBean.insertAfterColumnAction}" />
                    </td>
                  <% } else { %>
                    <td><h:commandButton id="insertbefore" value="Add"
                        action="#{userSessionBean.insertBeforeColumnAction}" />
                    </td>
                  <% } %>
                    <td><h:commandButton id="modify" value="Modify" 
                        action="#{userSessionBean.modifyColumnAction}" /></td>
                    <td><h:commandButton id="delete" value="Delete" 
                        action="#{userSessionBean.deleteColumnAction}" 
                        onclick="if (!confirm('You will lose all data entered. Are you sure?')) return false" /></td>
                    <td><h:commandButton id="back" value="Back" action="back" /></td>
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