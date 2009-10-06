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
	
  <%@ include file="/pages/templates/nciHeader.jsp" %>
	
  <tr>
      <td height="100%" valign="top">
      
           <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
			
               <%@ include file="/pages/templates/applicationHeader.jsp" %>
				
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
<h:form id="STANDARD_REPORTSForm">
	<tr>
		<td width="100%" valign="top"><br>
		<!-- target of anchor to skip menus --><a name="content" />
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="dataTablePrimaryLabel" height="20">REPORT TEMPLATES</td>
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
										<tr class="dataRowLight">
										

<td class="dataCellText">
	<h:selectOneMenu id="standardReportTemplateId" value="#{userSessionBean.selectedStandardReportTemplate}" valueChangeListener="#{userSessionBean.reportSelectionChanged}" immediate="true" onchange="submit()">
	     <f:selectItems value="#{userSessionBean.standardReportTemplateList}"/>
	</h:selectOneMenu>
</td>
<!--
											<td class="dataCellText"><SELECT NAME=reports SINGLE><OPTION>Structured Product Labeling (SPL) Report</OPTION><OPTION>FDA-UNII Subset Report (FDA UNII Code)</OPTION><OPTION>Individual Case Safety (ICS) Subset Report</OPTION><OPTION>Center for Devices and Radiological Health (CDRH) Subset Report</OPTION><OPTION>FDA-SPL Country Code Report</OPTION><OPTION>CDISC Subset Report</OPTION></SELECT></td>
										
-->										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="right" class="actionSection">
									<!-- bottom action buttons begins -->
									<table cellpadding="4" cellspacing="0" border="0">
										<tr>
											<td><h:commandButton id="add" action="#{userSessionBean.addReportTemplateAction}" value="Add" /></td>
											<td><h:commandButton id="modify" action="#{userSessionBean.modifyReportTemplateAction}" value="Modify" /></td>
											<td><h:commandButton id="edit" action="#{userSessionBean.editReportContentAction}" value="Edit Content" /></td>
											<td><h:commandButton id="generate" action="#{userSessionBean.generateStandardReportAction}" value="Generate" /></td>
											<!-- <td><h:commandButton id="review" action="review" value="Review" /></td> -->
											<td><h:commandButton id="delete" action="#{userSessionBean.deleteReportTemplateAction}" onclick="if (!confirm('You will lose all data entered. Are you sure?')) return false" value="Delete" /></td>
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
                
                                         <%@ include file="/pages/templates/applicationFooter.jsp" %>
                  
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
