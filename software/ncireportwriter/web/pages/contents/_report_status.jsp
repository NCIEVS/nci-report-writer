<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.utils.*" %>

<%
  String warningMsg = (String) request.getAttribute("warningMsg");
%>
<f:view>
  <h:form id="REPORT_STATUSForm">
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
                <table summary="" cellpadding="0" cellspacing="0" border="0">
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">REPORT STATUS</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                        <tr class="dataRowLight">
                          <td class="dataCellText">
                            <h:outputText value="Report Status" />
                          </td>
                          
                          <td class="dataCellText">
                            <h:selectOneListbox id="ReportStatusId" 
                                value="#{userSessionBean.selectedReportStatus}" 
                                valueChangeListener="#{userSessionBean.reportStatusSelectionChanged}" >
                              <f:selectItems value="#{userSessionBean.reportStatusList}" />
                            </h:selectOneListbox>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="add" action="add" value="Add" /></td>
                          <td><h:commandButton id="activate" value="Activate" 
                            action="#{userSessionBean.activateStatusAction}" /></td>
                          <td><h:commandButton id="inactivate" value="Inactivate" 
                            action="#{userSessionBean.inactivateStatusAction}" /></td>
                          <td><h:commandButton id="back" action="back" value="Back" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table> <!-- Table 1 (End) -->
        </td>
      </tr>
    </table> <!-- Table 1 (Begin) -->
  </h:form>
</f:view>