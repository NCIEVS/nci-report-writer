<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*"%>
<%
  String loginID = HTTPUtils.getJspAttributeString(request, "loginID");
  String message = HTTPUtils.getJspAttributeString(request, "standardMsg");
  String css = WebUtils.isUsingIE(request) ? "_IE" : "";
%>
<f:view>
  <h:form id="USER_UNLOCKForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0"
      width="100%" height="100%">
      <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top"><br/>
        <table summary="" cellpadding="0" cellspacing="0" border="0"
          width="725" class="contentPage">
          <!-- Table 2 (Begin) -->
          <tr>
            <td class="inputMessage">To unlock a user, select the user from the list
            below and then click the Submit button.<br/><br/>
            </td>
          </tr>
          <tr>
            <td>
              <span class="inputLabel">Login ID:&nbsp;</span>
              <input class="inputItem" name="loginID" value="<%=loginID%>">
            </td>
          </tr>
          <tr><td height="15"></td></tr>
          <tr>
            <td>
              <h:commandButton id="submit" value="Submit"
                action="#{userSessionBean.submitUnlockAction}" />
              <h:commandButton id="back" value="Back" action="back" />
            </td>
          </tr>
          <tr><td height="15"></td></tr>
          <tr>
            <td class="inputMessage">
              <% if (message != null) out.println(message); %>
            </td>
          </tr>
        </table>
        <!-- Table 2 (End) --></td>
      </tr>
    </table>
    <!-- Table 1 (End) -->
  </h:form>
</f:view>