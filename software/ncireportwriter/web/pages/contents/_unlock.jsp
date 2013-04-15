<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ page import="gov.nih.nci.evs.reportwriter.webapp.*"%>
<%@ page import="gov.nih.nci.evs.utils.*"%>
<%
  String loginID = HTTPUtils.getJspAttributeString(request, UserAccountRequest.LOGIN_ID);
  String warningMsg = HTTPUtils.getJspAttributeString(request, "warningMsg");
  String infoMsg = HTTPUtils.getJspAttributeString(request, "infoMsg");
  String css = WebUtils.isUsingIE(request) ? "_IE" : "";
%>
<f:view>
  <h:form id="USER_UNLOCKForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0">
      <tr><td height="10"></td></tr>
      <% if (warningMsg != null && warningMsg.trim().length() > 0) { %>
        <tr class="inputLabel"><td colspan="2" class="warningMsgColor">
          Warning:<br/>
          <%=StringUtils.toHtml(warningMsg)%><br/>
          <br/>
        </td></tr>
      <% } %>
      <% if (infoMsg != null && infoMsg.trim().length() > 0) { %>
        <tr class="inputLabel"><td colspan="2" class="fyiMsgColor">
          <%=StringUtils.toHtml(infoMsg)%><br/>
          <br/>
        </td></tr>
      <% } %>
      <tr>
        <td class="inputLabel">To unlock a user, enter the user's login ID
        below and then click the Unlock button.<br/><br/>
        </td>        
      </tr>     
    </table>      
    <table summary="" cellpadding="0" cellspacing="0" border="0">
      <tr><td height="5" colspan="3"></td></tr>      
      <tr>
        <td class="inputLabel">
          Login ID:&nbsp;
        </td>
        <td>
          <input class="inputItem" name="loginID" value="<%=loginID%>">&nbsp;&nbsp;
        </td>
      </tr>
      <tr><td height="5" colspan="3"></td></tr>
      <tr>
        <td></td>
        <td colspan="2">
          <h:commandButton id="submit" value="Unlock"
            action="#{userSessionBean.submitUnlockAccount}" />              
          <h:commandButton id="clear" value="Clear"
            action="#{userSessionBean.clearUnlockAccount}" />              
          <h:commandButton id="back" value="Back" action="back" />
        </td>
      </tr>      
    </table>
  </h:form>
</f:view>