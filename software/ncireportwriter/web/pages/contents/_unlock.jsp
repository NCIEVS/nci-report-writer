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
    <table summary="" cellpadding="0" cellspacing="0" border="0">
      <tr><td height="10"></td></tr>
      <tr>
        <td class="inputLabel">To unlock a user, select the user from the list
        below and then click the Submit button.<br/><br/>
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
        <td>  
          <h:commandButton id="submit" value="Unlock"
            action="#{userSessionBean.submitUnlockAction}" />              
        </td>
      </tr>
      <tr><td height="5" colspan="3"></td></tr>
      <tr>
        <td></td>
        <td colspan="2">
          <h:commandButton id="back" value="Back" action="back" />
        </td>
      </tr>      
    </table>
    <table summary="" cellpadding="0" cellspacing="0" border="0">
      <tr><td height="10"></td></tr>
      <tr>
        <td class="inputMessage" height="15px">
        <% if (message != null) out.print(message); else out.print("X&nbsp;"); %>
        </td>       
      </tr>  
    </table>    
  </h:form>
</f:view>