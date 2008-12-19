<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources" var="reportwriterBundle"/>

<%@ page contentType="text/html;charset=windows-1252"%>

<html>
<head>
<title>Home</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
<script src="script.js" type="text/javascript"></script>
</head>
<body>

<f:view>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
	
  <%@ include file="/pages/templates/nciHeader.html" %>
	
  <tr>
      <td height="100%" valign="top">
      
           <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
			
               <%@ include file="/pages/templates/applicationHeader.xhtml" %>
				
               <tr>
                    <td width="190" valign="top" class="subMenu">
          
<%@ include file="/pages/templates/sideMenu.html" %>
            
                    </td>
                    <td valign="top" width="100%">
                         <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
                              <tr>
                                    <td height="20" width="100%" class="mainMenu">
                
<%@ include file="/pages/templates/menuBar.xhtml" %>
                  
                                    </td>
                              </tr>
                              
                              <tr>
                                    <td>
                         
<!--_____ main content begins _____-->
<h:form id="ADD_STANDARD_REPORT_FIELDForm">
	<tr>
		<td width="100%" valign="top"><br>
		<!-- target of anchor to skip menus --><a name="content" />
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="dataTablePrimaryLabel" height="20">ADD STANDARD REPORT FIELD</td>
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
										<tr class="dataRowLight">
											<td class="dataCellText">Field Label</td>
											<td class="dataCellText"><input type="text" name="fieldlabel"></td>
										</tr>
										<tr class="dataRowDark">
										
											<td class="dataCellText">											
												<h:outputText value="Property Type" />
											</td>

											<td class="dataCellText">
												<h:selectOneMenu id="id" value="#{userSessionBean.selectedPropertyType}">
												     <f:selectItems value="#{userSessionBean.propertyTypeList}" />
												</h:selectOneMenu>											
											</td>											
										
										        <!--
											<td class="dataCellText">Property Type</td>
											<td class="dataCellText"><input type="text" name="propertytype"></td>
											-->
										</tr>
										<tr class="dataRowLight">
										        <!--
											<td class="dataCellText">Property Name</td>
											<td class="dataCellText"><input type="text" name="propertyname"></td>
										        -->
											<td class="dataCellText">
												<h:outputText value="Property Name" />
											</td>

											<td class="dataCellText">
												<h:selectOneMenu id="PropertyNameId" value="#{ontologyBean.selectedPropertyName}" valueChangeListener="#{ontologyBean.propertyNameSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.propertyNameList}" />
												</h:selectOneMenu>
											</td>										        
										        
										        
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText">Is Preferred?</td>
											<td class="dataCellText"><input type="radio" name="preferred" value="yes" checked>Yes&nbsp;<input type="radio" name="preferred" value="no">No<input type="radio" name="preferred" value="na">NA</td>
										</tr>
										<tr class="dataRowLight">
										        <!--
											<td class="dataCellText">Representational Form</td>
											<td class="dataCellText"><input type="text" name="representationalForm"></td>
											-->
										
											<td class="dataCellText">
												<h:outputText value="Representational Form" />
											</td>

											<td class="dataCellText">
												<h:selectOneMenu id="RepresentationalFormId" value="#{ontologyBean.selectedRepresentationalForm}" valueChangeListener="#{ontologyBean.representationalFormSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.representationalFormList}" />
												</h:selectOneMenu>
											</td>
											
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText">Source</td>
											<td class="dataCellText"><input type="text" name="source"></td>
										</tr>
										<tr class="dataRowLight">
										        <!--
											<td class="dataCellText">Qualifier Name</td>
											<td class="dataCellText"><input type="text" name="qualifiernname"></td>
											-->
											
											
											<td class="dataCellText">
												<h:outputText value="Property Qualifier" />
											</td>

											<td class="dataCellText">
												<h:selectOneMenu id="PropertyQualifierId" value="#{ontologyBean.selectedPropertyQualifier}" valueChangeListener="#{ontologyBean.propertyQualifierSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.propertyQualifierList}" />
												</h:selectOneMenu>
											</td>							
											
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText">Qualifier Value</td>
											<td class="dataCellText"><input type="text" name="qualifiervalue"></td>
										</tr>
										
										<!--
										<tr class="dataRowLight">
											<td class="dataCellText">Delimiter</td>
											<td class="dataCellText"><input type="text" name="delimeter" value="|"></td>
										</tr>
										-->
										
										
										<td class="dataCellText">
											<h:outputText value="Delimiter" />
										</td>

										<td class="dataCellText">
											<h:selectOneMenu id="DelimiterId" value="#{ontologyBean.selectedDelimiter}" valueChangeListener="#{ontologyBean.delimiterSelectionChanged}" >
												<f:selectItems value="#{ontologyBean.delimiterList}" />
											</h:selectOneMenu>
										</td>										
										
									</table>
								</td>
							</tr>
							<tr>
								<td align="right" class="actionSection">
									<!-- bottom action buttons begins -->
									<table cellpadding="4" cellspacing="0" border="0">
										<tr>
											<td><h:commandButton id="save" action="save" value="Save" /></td>
											<td><h:commandButton id="reset" action="reset" value="Reset" /></td>
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
                
                                         <%@ include file="/pages/templates/applicationFooter.html" %>
                  
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
    
        <%@ include file="/pages/templates/nciFooter.html" %>
    
       </td>
  </tr>
</table>

</f:view>
</body>
</html>