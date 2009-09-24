<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources" var="reportwriterBundle"/>

<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>

<html>
<head>
<title>Home</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
<script src="script.js" type="text/javascript"></script>
</head>
<body>
<%
  OntologyBean ontologyBean = BeanUtils.getOntologyBean();
  UserSessionBean userSessionBean = BeanUtils.getUserSessionBean();
  ReportColumn reportColumn = (ReportColumn) request.getAttribute("reportColumn");
  String columnNumber = "";
  String fieldlabel = "";
  ontologyBean.setSelectedDataCategory(null);
  userSessionBean.setSelectedPropertyType(null);
  ontologyBean.setSelectedPropertyName(null);
  ontologyBean.setSelectedRepresentationalForm(null);
  ontologyBean.setSelectedSource(null);
  ontologyBean.setSelectedPropertyQualifier(null);
  String qualifiervalue = "";
  ontologyBean.setSelectedDelimiter(null);
  String dependentfield = "";
  if (reportColumn != null) {
      columnNumber = reportColumn.getColumnNumber().toString();
      fieldlabel = reportColumn.getLabel();
      ontologyBean.setSelectedDataCategory(reportColumn.getFieldId());
      userSessionBean.setSelectedPropertyType(reportColumn.getPropertyType());
      ontologyBean.setSelectedPropertyName(reportColumn.getPropertyName());
      ontologyBean.setSelectedRepresentationalForm(reportColumn.getRepresentationalForm());
      ontologyBean.setSelectedSource(reportColumn.getSource());
      ontologyBean.setSelectedPropertyQualifier(reportColumn.getQualifierName());
      qualifiervalue = reportColumn.getQualifierValue();
      ontologyBean.setSelectedDelimiter(reportColumn.getDelimiter().toString());
      dependentfield = reportColumn.getConditionalColumnId().toString();
  }
%>
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
								<td class="dataTablePrimaryLabel" height="20">ADD REPORT FIELD</td>
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">

										<tr class="dataRowLight">
											<td class="dataCellText">Column Number</td>
											<td class="dataCellText">
											<input type="text" name="columnNumber" value="<%=columnNumber%>"></td>
										</tr>

										<tr class="dataRowDark">
											<td class="dataCellText">Field Label</td>
											<td class="dataCellText">
											<input type="text" name="fieldlabel" value="<%=fieldlabel%>"></td>
										</tr>

										<tr class="dataRowLight">
											<td class="dataCellText">Field Type</td>
											<td class="dataCellText">
												<h:selectOneMenu id="DataCategoryId" value="#{ontologyBean.selectedDataCategory}" valueChangeListener="#{ontologyBean.dataCategorySelectionChanged}" >
													<f:selectItems value="#{ontologyBean.dataCategoryList}" />
												</h:selectOneMenu>
											</td>									
										</tr> 

										<tr class="dataRowDark">
											<td class="dataCellText">Property Type</td>
											<td class="dataCellText">
												<h:selectOneMenu id="id" value="#{userSessionBean.selectedPropertyType}">
												     <f:selectItems value="#{userSessionBean.propertyTypeList}" />
												</h:selectOneMenu>											
											</td>											
										</tr>

										<tr class="dataRowLight">
											<td class="dataCellText">Property Name</td>
											<td class="dataCellText">
												<h:selectOneMenu id="PropertyNameId" value="#{ontologyBean.selectedPropertyName}" valueChangeListener="#{ontologyBean.propertyNameSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.propertyNameList}" />
												</h:selectOneMenu>
											</td>										        
										</tr>

										<tr class="dataRowDark">
											<td class="dataCellText">Is Preferred?</td>
											<td class="dataCellText">
      											<input type="radio" name="preferred" value="yes" >Yes&nbsp;
      											<input type="radio" name="preferred" value="no">No
      											<input type="radio" name="preferred" value="na" checked>NA
                                            </td>
										</tr>

										<tr class="dataRowLight">
											<td class="dataCellText">Representational Form</td>
											<td class="dataCellText">
												<h:selectOneMenu id="RepresentationalFormId" value="#{ontologyBean.selectedRepresentationalForm}" valueChangeListener="#{ontologyBean.representationalFormSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.representationalFormList}" />
												</h:selectOneMenu>
											</td>
										</tr>
          
										<tr class="dataRowDark">
											<td class="dataCellText">Source</td>
											<td class="dataCellText">
												<h:selectOneMenu id="SourceId" value="#{ontologyBean.selectedSource}" valueChangeListener="#{ontologyBean.sourceSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.sourceList}" />
												</h:selectOneMenu>
											</td>					
										</tr>

										<tr class="dataRowLight">
											<td class="dataCellText">Property Qualifier</td>
											<td class="dataCellText">
												<h:selectOneMenu id="PropertyQualifierId" value="#{ontologyBean.selectedPropertyQualifier}" valueChangeListener="#{ontologyBean.propertyQualifierSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.propertyQualifierList}" />
												</h:selectOneMenu>
											</td>							
										</tr>

										<tr class="dataRowDark">
											<td class="dataCellText">Qualifier Value</td>
											<td class="dataCellText">
    											<input type="text" name="qualifiervalue" value="<%=qualifiervalue%>">
                                            </td>
										</tr>
		
										<tr class="dataRowLight">
											<td class="dataCellText">Delimiter</td>
											<td class="dataCellText">
												<h:selectOneMenu id="DelimiterId" value="#{ontologyBean.selectedDelimiter}" valueChangeListener="#{ontologyBean.delimiterSelectionChanged}" >
													<f:selectItems value="#{ontologyBean.delimiterList}" />
												</h:selectOneMenu>
											</td>
										</tr>

										<tr class="dataRowDark">
											<td class="dataCellText">Dependent Field (if applicable)</td>
											<td class="dataCellText">
											     <input type="text" name="dependentfield" value="<%=dependentfield%>">
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
											<td><h:commandButton id="save" action="#{userSessionBean.saveReportColumnAction}" value="Save" /></td>
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