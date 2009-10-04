<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<f:view>
<h:form id="ADD_REPORT_STATUSForm">
    <tr>
        <td width="100%" valign="top"><br>
            <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
                <tr>
                    <td>
                        <table summary="" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td class="dataTablePrimaryLabel" height="20">ASSIGN REPORT STATUS</td>
                            </tr>
                            <tr>
                                <td>
                                    <table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
                                        <tr class="dataRowLight">
<td class="dataCellText">
    <h:outputText value="Report" />
</td>
                        

<td class="dataCellText">
    <h:selectOneMenu id="standardReportTemplateId" value="#{userSessionBean.selectedStandardReportTemplate_draft}" valueChangeListener="#{userSessionBean.reportSelectionChanged_draft}">
         <f:selectItems value="#{userSessionBean.standardReportTemplateList_draft}"/>
    </h:selectOneMenu>
</td>

                                        </tr>
                                        
                                        
                                        <tr class="dataRowLight">
<td class="dataCellText">
    <h:outputText value="Report Status" />
</td>

<td class="dataCellText">
    <h:selectOneMenu id="ReportStatusId" value="#{userSessionBean.selectedReportStatus}" valueChangeListener="#{userSessionBean.reportStatusSelectionChanged}" >
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
                                            <td><h:commandButton id="save" action="#{userSessionBean.assignStatusAction}" value="Save" /></td>
                                            <td><h:commandButton id="back" action="back" value="Back" /></td>
                                        </tr>
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