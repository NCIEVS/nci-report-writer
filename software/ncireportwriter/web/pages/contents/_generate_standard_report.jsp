<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:view>
  <h:form id="GENERATE_REPORTForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->  
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->  
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0"> <!-- Table 3 (Begin) -->  
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">GENERATE REPORT</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                        <tr class="dataRowLight">
                          <td class="dataCellText">Save as:</td>
                          <td class="dataCellText">
                            <input type="text" name="filename" size="50">
                              &nbsp;&nbsp;
                              <h:commandButton id="browse" value="Browse" 
                                  action="#{userSessionBean.selectFileAction}"/>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="generate" value="Generate" 
                              action="#{userSessionBean.generateStandardReportAction}" /></td>
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