<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources" var="reportwriterBundle"/>

<%@ page contentType="text/html;charset=windows-1252"%>

<html>
<head>
<title>Home</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
<script src="<%= request.getContextPath() %>/js/script.js" type="text/javascript"></script>

</head>
<body>

<f:view>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
	
  <%@ include file="/pages/templates/nci_header.jsp" %>
	
  <tr>
      <td height="100%" valign="top">
      
           <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
			
               <%@ include file="/pages/templates/application_header.jsp" %>
				
               <tr>
                    <td width="190" valign="top" class="subMenu">
          
<%@ include file="/pages/contents/side_menu.jsp" %>
            
                    </td>
                    <td valign="top" width="100%">
                         <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
                              <tr>
                                    <td height="20" width="100%" class="mainMenu">
                
<%@ include file="/pages/contents/menu_bar.jsp" %>
                  
                                    </td>
                              </tr>
                              
                              <tr>
                                    <td>
                         
<!--_____ main content begins _____-->
<h:form id="CREATE_STANDARD_REPORT_TEMPLATEForm">
	<tr>
		<td width="100%" valign="top"><br>
		<!-- target of anchor to skip menus --><a name="content" />
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="dataTablePrimaryLabel" height="20">CREATE REPORT TEMPLATE</td>
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
										
										<tr class="dataRowDark">

											<td class="dataCellText">Label</td>

											<td class="dataCellText"><input type="text" name="label"></td>
											
										</tr>
										
										<tr class="dataRowLight">
										
											<td class="dataCellText">											
												<h:outputText value="Coding Scheme:" />
											</td>

											<td class="dataCellText">
												<h:selectOneMenu id="ontologyId" value="#{ontologyBean.selectedOntology}" valueChangeListener="#{ontologyBean.ontologySelectionChanged}" >
												     <f:selectItems value="#{ontologyBean.ontologyList}" />
												</h:selectOneMenu>											
											</td>											
											
										</tr>
										
										<tr class="dataRowDark">

											<td class="dataCellText">Root Concept Code</td>

											<td class="dataCellText"><input type="text" name="rootConceptCode"></td>
										
											
										</tr>
										<tr class="dataRowLight">
										
											<td class="dataCellText">											
												<h:outputText value="Association Name" />
											</td>

											<td class="dataCellText">
												<h:selectOneMenu id="associationId" value="#{ontologyBean.selectedAssociation}" valueChangeListener="#{ontologyBean.associationSelectionChanged}" >
												     <f:selectItems value="#{ontologyBean.associationList}" />
												</h:selectOneMenu>											
											</td>											
									
										
										</tr>
										
										<!--
										<tr class="dataRowDark">
										
										
											<td class="dataCellText">Direction</td>
											
											<td class="dataCellText">											
												<h:selectOneRadio id="direction"
													value="#{ontologyBean.selectedDirection}">
													<f:selectItems value="#{ontologyBean.directionList}"/>
												</h:selectOneRadio>
											</td>

										</tr>
										-->
										
										<tr class="dataRowDark">
											<td class="dataCellText">Direction</td>
											<td class="dataCellText">
											<input type="radio" name="direction" value="source" checked>Source&nbsp;
											<input type="radio" name="direction" value="target">Target</td>
										</tr>										
										
										<tr class="dataRowLight">


											<td class="dataCellText">
												<h:outputText value="Level" />
											</td>

											<td class="dataCellText">
												<h:selectOneMenu id="LevelId" value="#{ontologyBean.selectedLevel}" valueChangeListener="#{ontologyBean.levelSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.levelList}" />
												</h:selectOneMenu>
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
											<td><h:commandButton id="save" action="#{userSessionBean.saveTemplateAction}" value="Save" /></td>
											<td><input type="reset" value="Reset" /></td>
											<td><h:commandButton id="back" action="back" value="Back" /></td>
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
<!--_____ main content ends _____-->


                                    </td>
                              </tr>
 
 <tr><td>&nbsp;&nbsp;</td></tr>
 
                               <tr>
                                    <td height="20" width="100%" class="footerMenu">
                
                                         <%@ include file="/pages/templates/application_footer.jsp" %>
                  
                                    </td>
                              </tr>
                        </table>
                    </td>
               </tr>
           </table>
       </td>
  </tr>
  <tr>
       <td>
    
        <%@ include file="/pages/templates/nciFooter.jsp" %>
    
       </td>
  </tr>
</table>

</f:view>
</body>
</html>
