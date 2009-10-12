<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources" var="reportwriterBundle"/>

<f:view>
  <h:form id="loginForm">                  
    <table summary="" cellpadding="0" cellspacing="0" border="0" height="500">
      <tr>
        <td valign="top">
  
  <%@ include file="/pages/contents/welcome.jsp" %>
  
        </td>
        <td valign="top">
          
          <!-- === Sidebar (Begin) =========================================== -->
          <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
            
            <!-- === Login (Begin) =========================================== -->
            <tr>
              <td valign="top">
                <table summary="" cellpadding="2" cellspacing="0" border="0" 
                    width="100%" class="sidebarSection">
                <% 
                  String queryString = request.getQueryString();
                  if (queryString != null && queryString.contains("logout=true")) { 
                %>
                  <tr>
                    <td class="sidebarTitle" height="20">LOGOUT REPORT WRITER</td>
                  </tr>
                  <tr>
                    <td class="txtHighlight">You have successfully logged out.</td>
                  </tr>                                      
                <% } else { %>
                  <tr>
                    <td class="sidebarTitle" height="20">LOGIN TO REPORT WRITER</td>
                  </tr>
                <% } %>
                <% if (request.getAttribute("loginWarning") != null) { %>                                      
                  <tr>
                    <td class="txtHighlight"><%=request.getAttribute("loginWarning")%></td>
                  </tr>
                <% }%>
    
                  <tr>
                    <td class="sidebarContent">
                      <table cellpadding="2" cellspacing="0" border="0">
                        <tr>
                          <td class="sidebarLogin" align="right">
                            <label for="loginID">LOGIN ID</label>
                          </td>                                        
                          <td>
                            <h:inputText id="userid" size="15" required="false" 
                                value="#{loginBean.userid}" >  
                              <f:validateLength minimum="0" maximum="50" />
                            </h:inputText>  
                          </td> 
                        </tr>
    
                        <tr>
                          <td class="sidebarLogin" align="right">
                            <label for="password">PASSWORD</label>
                          </td>                                        
                          <td>
                            <h:inputSecret id="password" size="15" required="false" 
                                value="#{loginBean.password}" >
                              <f:validateLength minimum="0" maximum="50" />
                            </h:inputSecret>
                          </td>
                        </tr>
    
                        <tr>
                          <td>&nbsp;</td>
                          <td>                                         
                            <h:commandButton id="submit" action="#{loginBean.loginAction}"
                                value="#{reportwriterBundle.loginSubmitLabel}">
                            </h:commandButton>
                          </td>                                         
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <!-- === Login (End) ============================================= -->
  
  <%@ include file="/pages/contents/whats_new.jsp" %>
  
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