<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<f:view>
  <h:form id="MESSAGEForm">
    <tr>
        <td width="100%" valign="top">
            <br>
            <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
                <tr>
                    <td>
                        <table summary="" cellpadding="0" cellspacing="0" border="0">
                        <!--
                            <tr>
                                <td class="dataTablePrimaryLabel" height="20">MESSAGE</td>
                            </tr>
                        --> 
                            <tr>
                                <td>
                                    <table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
                                        <tr>
                                            <td class="sidebarContent">
                                            <%
                                            String message = (String) request.getSession().getAttribute("message");
                                            message = StringUtils.toHtml(message);
                                            %>
                                            
                                            <%=message%>
                                            
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" class="actionSection">
                                    <!-- bottom action buttons begins -->
                                    <table cellpadding="4" cellspacing="0" border="0">
                                        <tr>
                                            <td><input type="button" value="Back" onClick="history.go(-1);return true;"></td>
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
