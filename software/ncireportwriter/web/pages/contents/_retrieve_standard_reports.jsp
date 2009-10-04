<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:view>
<h:form id="STANDARD_REPORTSForm">
    <tr>
        <td width="100%" valign="top">
            <br>
            <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
                <tr>
                    <td>
                        <table summary="" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td class="dataTablePrimaryLabel" height="20">RETRIEVE REPORT</td>
                            </tr>
                            <tr>
                                <td>
                                    <table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
                                        <tr class="dataRowLight">
                                        

<td class="dataCellText">
    <h:selectOneMenu id="standardReportId" value="#{userSessionBean.selectedStandardReportTemplate}" valueChangeListener="#{userSessionBean.reportSelectionChanged}">
         <f:selectItems value="#{userSessionBean.standardReportTemplateList}"/>
    </h:selectOneMenu>
</td>
<!--
                                            <td class="dataCellText"><SELECT NAME=reports SINGLE><OPTION>Structured Product Labeling (SPL) Report</OPTION><OPTION>FDA-UNII Subset Report (FDA UNII Code)</OPTION><OPTION>Individual Case Safety (ICS) Subset Report</OPTION><OPTION>Center for Devices and Radiological Health (CDRH) Subset Report</OPTION><OPTION>FDA-SPL Country Code Report</OPTION><OPTION>CDISC Subset Report</OPTION></SELECT></td>
                                        
-->                                     </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" class="actionSection">
                                    <!-- bottom action buttons begins -->
                                    <table cellpadding="4" cellspacing="0" border="0">
                                        <tr>
                                            <td><h:commandButton id="download" action="#{userSessionBean.downloadReportAction}" value="Download" /></td>
                                            <td><h:commandButton id="back" action="back" value="Back" /></td>
                                        </tr>
                                    </table>
                                    <!-- bottom action buttons end -->
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