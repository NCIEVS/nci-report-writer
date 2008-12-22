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
<h:form id="STANDARD_REPORT_FIELDSForm">
	<tr>
		<td width="100%" valign="top"><br>
		<!-- target of anchor to skip menus --><a name="content" />
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="dataTablePrimaryLabel" height="20">STANDARD REPORT FIELDS</td>
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
										<tr>
											<th class="dataTableHeader" scope="col" align="center">Data Field</th>
											<th class="dataTableHeader" scope="col" align="center">Field No</th>
											<th class="dataTableHeader" scope="col" align="center">Field Label</th>
											<th class="dataTableHeader" scope="col" align="center">Field Type</th>
											<th class="dataTableHeader" scope="col" align="center">Property Type</th>
											<th class="dataTableHeader" scope="col" align="center">Property Name</th>
											<th class="dataTableHeader" scope="col" align="center">Is Preferred?</th>
											<th class="dataTableHeader" scope="col" align="center">Representational Form</th>
											<th class="dataTableHeader" scope="col" align="center">Source</th>
											<th class="dataTableHeader" scope="col" align="center">Qualifier Name</th>
											<th class="dataTableHeader" scope="col" align="center">Qualifier Value</th>
											<th class="dataTableHeader" scope="col" align="center">Delimiter</th>
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="1"></td>
											<td class="dataCellNumerical">1</td>
											<td class="dataCellText">Contributing Source</td>
											<td class="dataCellText">Property</td>
											<td class="dataCellText">Generic</td>
											<td class="dataCellText">Contributing Source</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="2"></td>
											<td class="dataCellNumerical">2</td>
											<td class="dataCellText">Subset Code</td>
											<td class="dataCellText">Associated Concept Code</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="3"></td>
											<td class="dataCellNumerical">3</td>
											<td class="dataCellText">Subset Name</td>
											<td class="dataCellText">Associated Concept Property</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">Preferred_Name</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="4"></td>
											<td class="dataCellNumerical">4</td>
											<td class="dataCellText">Concept Code</td>
											<td class="dataCellText">Code</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="5"></td>
											<td class="dataCellNumerical">5</td>
											<td class="dataCellText">Source Code</td>
											<td class="dataCellText">Property Qualifier</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">FULL_SYN</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">PT</td>
											<td class="dataCellText">FDA</td>
											<td class="dataCellText">source-code</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="6"></td>
											<td class="dataCellNumerical">6</td>
											<td class="dataCellText">Source PT</td>
											<td class="dataCellText">Property</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">FULL_SYN</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">PT</td>
											<td class="dataCellText">FDA</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="7"></td>
											<td class="dataCellNumerical">7</td>
											<td class="dataCellText">Source Synonym(s)</td>
											<td class="dataCellText">Property</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">FULL_SYN</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">SY</td>
											<td class="dataCellText">FDA</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">tab</td>
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="8"></td>
											<td class="dataCellNumerical">8</td>
											<td class="dataCellText">Source Definition</td>
											<td class="dataCellText">Property</td>
											<td class="dataCellText">Definition</td>
											<td class="dataCellText">ALT_DEFINITION</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">FDA</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="9"></td>
											<td class="dataCellNumerical">9</td>
											<td class="dataCellText">NCI Definition</td>
											<td class="dataCellText">Property</td>
											<td class="dataCellText">Definition</td>
											<td class="dataCellText">DEFINITION</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">NCI</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="10"></td>
											<td class="dataCellNumerical">10</td>
											<td class="dataCellText">Parent Code</td>
											<td class="dataCellText">Parent Code</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="11"></td>
											<td class="dataCellNumerical">11</td>
											<td class="dataCellText">Parent Source Code</td>
											<td class="dataCellText">Parent Property Qualifier</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">FULL_SYN</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">PT</td>
											<td class="dataCellText">FDA</td>
											<td class="dataCellText">source-code</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="12"></td>
											<td class="dataCellNumerical">12</td>
											<td class="dataCellText">Parent Source PT</td>
											<td class="dataCellText">Parent Property</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">FULL_SYN</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">PT</td>
											<td class="dataCellText">FDA</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="13"></td>
											<td class="dataCellNumerical">13</td>
											<td class="dataCellText">Parent NCI PT</td>
											<td class="dataCellText">Parent Property</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">FULL_SYN</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">PT</td>
											<td class="dataCellText">NCI</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowDark">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="14"></td>
											<td class="dataCellNumerical">14</td>
											<td class="dataCellText">2nd Parent Code</td>
											<td class="dataCellText">Parent Code</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
										<tr class="dataRowLight">
											<td class="dataCellText"><input type="radio" name="selectedcolumn" value="15"></td>
											<td class="dataCellNumerical">15</td>
											<td class="dataCellText">2nd Parent NCI PT</td>
											<td class="dataCellText">Parent Property</td>
											<td class="dataCellText">Presentation</td>
											<td class="dataCellText">FULL_SYN</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">PT</td>
											<td class="dataCellText">NCI</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
											<td class="dataCellText">null</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="right" class="actionSection">
									<!-- bottom action buttons begins -->
									<table cellpadding="4" cellspacing="0" border="0">
										<tr>
											<td><h:commandButton id="insertbefore" action="insertbefore" value="InsertBefore" /></td>
											<td><h:commandButton id="insertafter" action="insertafter" value="InsertAfter" /></td>
											<td><h:commandButton id="modify" action="modify" value="Modify" /></td>
											<td><h:commandButton id="delete" action="delete" value="Delete" /></td>
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

