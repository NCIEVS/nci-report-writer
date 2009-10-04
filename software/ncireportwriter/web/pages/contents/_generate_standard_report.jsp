<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:view>
  <h:form id="GENERATE_REPORTForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->  
    <tr>
        <td width="100%" valign="top"><br>
            <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
                <tr>
                    <td>
                        <table summary="" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td class="dataTablePrimaryLabel" height="20">GENERATE REPORT</td>
                            </tr>
                            <tr>
                                <td>
                                    <table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
                                        <tr class="dataRowLight">
                                            <td class="dataCellText">Save as:</td>
                                            <td class="dataCellText"><input type="text" name="filename" size=50>&nbsp;&nbsp;<h:commandButton id="browse" action="#{userSessionBean.selectFileAction}" value="Browse" /></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" class="actionSection">
                                    <!-- bottom action buttons begins -->
                                    <table cellpadding="4" cellspacing="0" border="0">
                                        <tr>
                                            <td><h:commandButton id="generate" action="#{userSessionBean.startReportGenerationAction}" value="Generate" /></td>
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
    </table> <!-- Table 1 (End) -->  
  </h:form>
</f:view>