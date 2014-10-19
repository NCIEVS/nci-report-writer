<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<%@ page import="gov.nih.nci.evs.utils.*" %>

<%
String css = WebUtils.isUsingIE(request) ? "_IE" : "";
%>

<f:view>
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
                    <td class="dataTablePrimaryLabel" height="20">GENERATE HIERARCHY REPORT</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Specify the corresponding Excel file here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                        <tr class="dataRowLight">
                            <td class="dataCellText">
				<form id="GENERATE_HIERARCHY_REPORTForm" enctype="multipart/form-data" method="post" action="<%= request.getContextPath() %>/upload?action=hierarchy" >
				<p>
			
				Please specify a file:<br>
				<input type="file" id="fileInput" name="fileInput" size='50' >
				</p>
				<div>
				<INPUT TYPE="submit" value="Submit" ALT="Submit Form">
				</div>
				
				
				</form>                       
                          </td>
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

</f:view>