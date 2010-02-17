<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*"%>
<%@ page import="gov.nih.nci.evs.reportwriter.webapp.*"%>
<%
  final String PROBLEM = AccessDeniedRequest.PROBLEM;
  final String LOGIN_ID = AccessDeniedRequest.LOGIN_ID;
  final String EMAIL = AccessDeniedRequest.EMAIL;
  final String INFORMATION = AccessDeniedRequest.INFORMATION;

  String[] problems = new String[] { 
   "I am locked out of my account", 
   "I forgot my password",
   "My password does not work",
  }; 
  String problem = HTTPUtils.getJspAttributeString(request, PROBLEM);
  String loginID = HTTPUtils.getJspAttributeString(request, LOGIN_ID);
  String email = HTTPUtils.getJspAttributeString(request, EMAIL);
  String information = HTTPUtils.getJspAttributeString(request, INFORMATION);
  String warningMsg = (String) request.getAttribute("warningMsg");
  String infoMsg = (String) request.getAttribute("infoMsg");
  String css = WebUtils.isUsingIE(request) ? "_IE" : "";
%>
<f:view>
  <h:form id="USER_REQUESTForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0"
      width="100%" height="100%">
      <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top"><br/>
        <table summary="" cellpadding="0" cellspacing="0" border="0"
          width="725" class="contentPage">
          <!-- Table 2 (Begin) -->
          <% if (warningMsg != null && warningMsg.trim().length() > 0) { %>
            <tr><td colspan="2" class="warningMsgColor">
              Warning:<br/>
              <%=StringUtils.toHtml(warningMsg)%><br/>
              <br/>
            </td></tr>
          <% } %>
          <% if (infoMsg != null && infoMsg.trim().length() > 0) { %>
            <tr><td colspan="2" class="fyiMsgColor">
              <%=StringUtils.toHtml(infoMsg)%><br/>
              <br/>
            </td></tr>
          <% } %>
          <tr>
            <td colspan="2">We apologize for any inconvenience
            you're experiencing by not being able to access your
            account.<br/>
            To resolve this issue as quickly as possible, please provide
            us with the following information:<br/>
            <br/>
            </td>
          </tr>
          <tr>
            <td colspan="2">What's the problem you are
            experiencing? <i class="warningMsgColor">*</i><br/>
            <%
               for (int i=0; i<problems.length; ++i) {
                 String checked = "";
                 if (problem != null && problem.equals(problems[i]))
                    checked = "checked=\"checked\"";
            %>
                 <input type="radio" name="problem" <%=checked%>
                   value="<%=problems[i]%>"> <%=problems[i]%><br/>
            <%
               }
            %> <br/>
            </td>
          </tr>
          <tr>
            <td class="requestLabel">Login ID: <i
              class="warningMsgColor">*</i></td>
            <td><input class="requestPageTF<%=css%>"
              name="loginID" alt="loginID" value="<%=loginID%>"></td>
          </tr>
          <tr>
            <td class="requestLabel">Email: <i
              class="warningMsgColor">*</i></td>
            <td><input class="requestPageTF<%=css%>"
              name="email" alt="email" value="<%=email%>"></td>
          </tr>
          <tr>
            <td class="requestLabel">Additional<br/>
            Information:</td>
            <td><textarea class="requestPageTA<%=css%>"
              name="information"><%=information%></textarea></td>
          </tr>
          <tr>
            <td class="requestLabel"><i class="warningMsgColor">*
            Required</i><br/><br/></td>
          </tr>
          <tr>
            <td colspan="2">
              <h:commandButton id="submit" value="Submit"
                action="#{userSessionBean.submitAccessDenied}" />
              <h:commandButton id="clear" value="Clear"
                action="#{userSessionBean.clearAccessDenied}" />
              <h:commandButton id="back" value="Back" action="back" />
            </td>
          </tr>
        </table>
        <!-- Table 2 (End) --></td>
      </tr>
    </table>
    <!-- Table 1 (End) -->
  </h:form>
</f:view>