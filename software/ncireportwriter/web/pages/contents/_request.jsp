<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="java.util.*" %>

<%
  String[] problems = new String[] { 
   "I am locked out of my account", 
   "I forgot my password",
   "My password does not work",
  }; 
%>

<%--
<f:view>
  <h:form id="AccountRequestForm">
--%>
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->
            <tr>
              <td colspan="2">
                We apologize for any inconvenience you're experiencing by not
                being able to access your account.<br/>
                To resolve this issue as quickly as possible, please provide
                us with the following information:<br/>
                <br/>
              </td>
            </tr>
            
            <tr>
              <td colspan="2">
                What's the problem you are experiencing? <i class="warningMsgColor">*</i><br/>
                <%
                  for (int i=0; i<problems.length; ++i) {
                %>
                    <input type="radio" name="request" value="<%=problems[i]%>">
                      <%=problems[i]%><br/>
                <%
                  }
                %>
                <br/>
              </td>
            </tr>
            
            <tr>
              <td width="75" valign="top">Login ID: <i class="warningMsgColor">*</i></td>
              <td><input type="text" name="userid"></td>
            </tr>
            
            <tr>
              <td width="75" valign="top">Additional<br/>Information:</td>
              <td><textarea name="additionalInformation"></textarea></td>
            </tr>

            <tr>
              <td class="newConceptNotes"><i class="warningMsgColor">* Required</i></td>
            </tr>
<%--
            <tr>
              <td><h:commandButton id="back" value="Back" action="back" /></td>
            </tr>
--%>            
          </table> <!-- Table 2 (End) -->
        </td>
      </tr>
    </table> <!-- Table 1 (End) -->
<%--
  </h:form>
</f:view>
--%>
