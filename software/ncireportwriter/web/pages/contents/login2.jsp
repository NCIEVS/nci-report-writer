<f:view>

           <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
            
               <tr>
                    <td width="190" valign="top" class="subMenu">
          
<%@ include file="/pages/templates/sideMenu.jsp" %>
            
                    </td>
                    <td valign="top" width="100%">
                         <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
                              <tr>
                                    <td height="20" width="100%" class="mainMenu">

<%@ include file="/pages/templates/menuBar_skip.jsp" %>

                                    </td>
                              </tr>
                             
              
<!--_____ main content begins _____-->


                              <tr>
                                    <td width="100%" valign="top">
                  
                  <!-- target of anchor to skip menus --><a name="content" />
                  
                                          <table summary="" cellpadding="0" cellspacing="0" border="0" width="600" height="100%">
                    
                            <!-- banner begins -->
                            <tr>
                                  <td class="bannerHome"><img src="<%= request.getContextPath() %>/images/bannerHome.gif" height="140" width="620"></td>
                            </tr>
                            <!-- banner begins -->

                    <tr>
                      <td height="100%">
                        <!-- target of anchor to skip menus --><a name="content" />
                        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                          <tr>
                            <td>


<%@ include file="/pages/templates/welcome.jsp" %>

                            
                            </td>
                            <td valign="top">
                              
                              <!-- sidebar begins -->
                              <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                                
                                
                                
                                <!-- login begins -->
                                <tr>
                                  <td valign="top">
                                  
 
 
<h:form id="loginForm">                                  
                                    <table summary="" cellpadding="2" cellspacing="0" border="0" width="100%" class="sidebarSection">
                                      
<% 
if(request.getQueryString() != null && request.getQueryString().equals("logout")) { 
%>

                                      <tr>
                                        <td class="sidebarTitle" height="20">LOGOUT REPORT WRITER</td>
                                      </tr>
                                      
                                      <tr>
                                        <td class="txtHighlight">You have successfully logged out.</td>
                                      </tr>                                      
                                      
<%} else { %>
                                      
                                      <tr>
                                        <td class="sidebarTitle" height="20">LOGIN TO REPORT WRITER</td>
                                      </tr>
<%} %>
                                      
                                      <tr>
                                        <td class="sidebarContent">
                                          <table cellpadding="2" cellspacing="0" border="0">
                                            <tr>
                                            
    <td class="sidebarLogin" align="right"><label for="loginID">LOGIN ID</label></td>                                        
    <td>
        <h:inputText id="userid" size="15" required="false" value="#{loginBean.userid}" >  
           <f:validateLength minimum="0" maximum="50" />
        </h:inputText>  
    </td> 
        
                                            </tr>
                                            <tr>
                                            
    <td class="sidebarLogin" align="right"><label for="password">PASSWORD</label></td>                                        
    <td><h:inputSecret id="password" size="15" required="false" value="#{loginBean.password}" >
    <f:validateLength minimum="0" maximum="50" /></h:inputSecret> </td>
                                            
                                            </tr>
                                            <tr>
                                              <td>&nbsp;</td>
     <td>                                         
     <h:commandButton id="submit" action="#{loginBean.loginAction}" value="#{reportwriterBundle.loginSubmitLabel}"></h:commandButton>
     </td>                                         
                                            </tr>
                                          </table>
                                        </td>
                                      </tr>
                                    </table>
                                    
</h:form>                                    
                                    
                                    
                                  </td>
                                </tr>
                                <!-- login ends -->
                                
                                
                                
<%@ include file="/pages/templates/whatsNews.jsp" %>
                                
                                
                                
                                <!-- spacer cell begins (keep for dynamic expanding) -->
                                <tr>
                                  <td valign="top" height="100%">
                                    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
                                       <tr>
                                          <td class="sidebarContent" valign="top">&nbsp;</td>
                                       </tr>
                                    
                                    </table>
                  </td>
                    </tr>
                                <!-- spacer cell ends -->
                                                                
                                    </table>
                                     <!-- sidebar ends -->
                              
                              </td>
                            </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
<!--_____ main content ends _____-->
              
            </table>
          </td>
        </tr>
      </table>


</f:view>
