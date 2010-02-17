<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*"%>
<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources"
  var="reportwriterBundle" />
<%
  String pagesPath = FormUtils.getPagesPath(request);
  String loginWarning = (String)request.getAttribute("loginWarning");
%>
<f:view>
  <h:form id="loginForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0"
      height="500">
      <tr>
        <td valign="top"><jsp:include
          page="/pages/contents/welcome.jsp" /></td>
        <td valign="top"><!-- === Sidebar (Begin) =========================================== -->
        <table summary="" cellpadding="0" cellspacing="0" border="0"
          height="100%">
          <!-- === Login (Begin) =========================================== -->
          <tr>
            <td valign="top">
            <table summary="" cellpadding="2" cellspacing="0" border="0"
              width="100%" class="sidebarSection">
              <% 
                  String queryString = request.getQueryString();
                  if (queryString != null && queryString.contains("logout")) { 
                	  loginWarning = "You have successfully logged out.";
                %>
              <tr>
                <td class="sidebarTitle" height="20">LOGOUT REPORT
                WRITER</td>
              </tr>
              <% } else { %>
              <tr>
                <td class="sidebarTitle" height="20">LOGIN TO
                REPORT WRITER</td>
              </tr>
              <% } %>              
              <tr>
                <td class="sidebarContent">
                  <span class="txtHighlight">
                  <%
                  if (loginWarning == null || loginWarning.equalsIgnoreCase("null"))
                	  out.print("&nbsp;");
                  else
                    out.print(loginWarning);
                  %>
                  </span>
                </td>
              </tr>              
              <tr>
                <td class="sidebarContent">
                <table cellpadding="2" cellspacing="0" border="0">
                  <tr>
                    <td class="sidebarLogin" align="right"><label
                      for="loginID">LOGIN ID</label></td>
                    <td><h:inputText id="userid" size="15"
                      required="false" value="#{loginBean.userid}">
                      <f:validateLength minimum="0" maximum="50" />
                    </h:inputText></td>
                  </tr>
                  <tr>
                    <td class="sidebarLogin" align="right"><label
                      for="password">PASSWORD</label></td>
                    <td><h:inputSecret id="password" size="15"
                      required="false" value="#{loginBean.password}">
                      <f:validateLength minimum="0" maximum="50" />
                    </h:inputSecret></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td><h:commandButton id="submit"
                      action="#{loginBean.loginAction}"
                      value="#{reportwriterBundle.loginSubmitLabel}">
                    </h:commandButton></td>
                  </tr>
                  <tr>
                    <td colspan="2" align="center" nowrap><a
                      href="<%=pagesPath%>/main/request.jsf">Can't
                    access your account?</a></td>
                  </tr>
                </table>
                </td>
              </tr>
            </table>
            </td>
          </tr>
          <!-- === Login (End) ============================================= -->

          <jsp:include page="/pages/contents/whats_new.jsp" />

          <!-- === Spacer Cell (Begin): keep for dynamic expanding ========= -->
          <tr>
            <td valign="top" height="100%">
            <table summary="" cellpadding="0" cellspacing="0" border="0"
              width="100%" height="100%" class="sidebarSection">
              <tr>
                <td class="sidebarContent" valign="top">&nbsp;</td>
              </tr>
            </table>
            </td>
          </tr>
          <!-- === Spacer Cell (End) ======================================= -->

        </table>
        <!-- === Sidebar (End) ============================================= -->

        </td>
      </tr>
    </table>
  </h:form>
</f:view>