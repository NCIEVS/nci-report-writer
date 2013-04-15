<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:view>
  <h:form id="ADD_REPORT_STATUSForm">
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
                    <td class="dataTablePrimaryLabel" height="20">ASSIGN REPORT STATUS</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                        <tr class="dataRowLight">
                          <td class="dataCellText">
                            <h:outputText value="Report" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="standardReportTemplateId" 
                                value="#{userSessionBean.selectedStandardReportTemplate_draft}" 
                                valueChangeListener="#{userSessionBean.reportSelectionChanged_draft}">
                              <f:selectItems value="#{userSessionBean.standardReportTemplateList_draft}"/>
                            </h:selectOneMenu>
                          </td>
                        </tr>
  
                        <tr class="dataRowLight">
                          <td class="dataCellText">
                            <h:outputText value="Report Status" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="ReportStatusId" 
                                value="#{userSessionBean.selectedReportStatus}" 
                                valueChangeListener="#{userSessionBean.reportStatusSelectionChanged}" >
                              <f:selectItems value="#{userSessionBean.reportStatusList}" />
                            </h:selectOneMenu>
                          </td>
                        </tr>                                       
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="save" value="Save" 
                              action="#{userSessionBean.assignStatusAction}" /></td>
                          <td><h:commandButton id="back" action="back" value="Back" /></td>
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