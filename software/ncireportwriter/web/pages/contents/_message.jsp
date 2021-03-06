<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.utils.*" %>

<%
  String message = (String) request.getSession().getAttribute("message");
  String goBackAttr = (String) request.getAttribute("goBack");
  int goBack = (goBackAttr != null) ? Integer.parseInt(goBackAttr) : 1;
  goBack *= -1;
%>
<f:view>
  <h:form id="MESSAGEForm">
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
                    <!-- Table 3 (Begin) -->
                  <!--
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">MESSAGE</td>
                  </tr>
                  --> 
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                        <tr>
                          <td class="sidebarContent">
                            <%=StringUtils.toHtml(message)%>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><input type="button" value="Back" onClick="history.go(<%=goBack%>);return true;"></td>
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
