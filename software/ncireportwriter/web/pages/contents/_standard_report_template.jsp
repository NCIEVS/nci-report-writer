<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<%
  String warningMsg = (String) request.getAttribute("warningMsg");
%>

<f:view>
  <h:form id="SELECT_TASKForm">
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
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">REPORT TEMPLATES</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" cellpadding="3" 
                          cellspacing="0" border="0" class="dataTable" width="100%">
                        <tr class="dataRowLight">
                          <td class="dataCellText">
                            <h:selectOneMenu id="standardReportTemplateId" 
                                value="#{userSessionBean.selectedStandardReportTemplate}" 
                                valueChangeListener="#{userSessionBean.reportSelectionChanged}" 
                                immediate="true" onchange="submit()">
                              <f:selectItems value="#{userSessionBean.standardReportTemplateList}"/>
                            </h:selectOneMenu>
                          </td>
                          <!--
                          <td class="dataCellText">
                            <SELECT NAME=reports SINGLE>
                              <OPTION>Structured Product Labeling (SPL) Report</OPTION>
                              <OPTION>FDA-UNII Subset Report (FDA UNII Code)</OPTION>
                              <OPTION>Individual Case Safety (ICS) Subset Report</OPTION>
                              <OPTION>Center for Devices and Radiological Health (CDRH) Subset Report</OPTION>
                              <OPTION>FDA-SPL Country Code Report</OPTION>
                              <OPTION>CDISC Subset Report</OPTION>
                            </SELECT>
                          </td>
                          -->                                     
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="add" value="Add" 
                              action="#{userSessionBean.addReportTemplateAction}" /></td>
                          <td><h:commandButton id="modify" value="Modify" 
                              action="#{userSessionBean.modifyReportTemplateAction}" /></td>
                          <td><h:commandButton id="edit" value="Edit Content" 
                              action="#{userSessionBean.editReportContentAction}" /></td>
                          <td><h:commandButton id="generate" value="Generate" 
                              action="#{userSessionBean.generateStandardReportAction}" /></td>
                          <!-- <td><h:commandButton id="review" value="Review" action="review" /></td> -->
                          <td><h:commandButton id="delete" value="Delete" 
                              action="#{userSessionBean.deleteReportTemplateAction}" 
                              onclick="if (!confirm('You will lose all data entered. Are you sure?')) return false" /></td>
                          <td><h:commandButton id="back" value="Back" action="back" /></td>
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