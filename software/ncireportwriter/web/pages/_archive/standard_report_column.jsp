<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources" var="reportwriterBundle"/>

<%@ page contentType="text/html;charset=windows-1252"%>

<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<html>
<head>
<title>Home</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet_r568.css" />
<script src="script.js" type="text/javascript"></script>
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
                         
<!-- _____ main content begins _____ -->
<h:form id="STANDARD_REPORT_FIELDSForm">

<%
Object[] objs = null;
%>

	<tr>
		<td width="100%" valign="top"><br>
		<!-- target of anchor to skip menus -->
		<a name="content" />
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
                <% if (request.getAttribute("warningMsg") != null) { %>
                  <tr><td class="warningMsgColor">
                    Warning:<br/>
                    <%=StringUtils.toHtml((String) request.getAttribute("warningMsg"))%><br/>
                    <br/>
                  </td></tr>
                <% } %>
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
							<!--
								<td class="dataTablePrimaryLabel" height="20">REPORT FIELDS</td>
							-->	
								<td class="dataTablePrimaryLabel" height="20">
								<% 
								    String templat_label = (String) request.getSession().getAttribute("selectedStandardReportTemplate");
								%>
								    <%= templat_label %>
								</td>
								
								
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
										<tr>
											<th class="dataTableHeader" scope="col" align="center">Data Field</th>
											<th class="dataTableHeader" scope="col" align="center">Column Number</th>
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
											<th class="dataTableHeader" scope="col" align="center">Dependency</th>
										</tr>
										<% 
											   try{
											   
String templatLabel = (String) request.getSession().getAttribute("selectedStandardReportTemplate");


												  SDKClientUtil sdkclientutil = new SDKClientUtil();
												  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
												  String methodName = "setLabel";
												  Object obj = sdkclientutil.search(FQName, methodName, templatLabel);
												  StandardReportTemplate standardReportTemplate = (StandardReportTemplate) obj;

											          java.util.Collection cc = standardReportTemplate.getColumnCollection();
											          
											          if (cc != null) {
											          objs = cc.toArray();
											          if (objs.length > 0) {
												  for(int i=0; i<objs.length; i++) {
												      gov.nih.nci.evs.reportwriter.bean.ReportColumn c = (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];


										%>
												<tr>
													<td class="dataCellText"> <input type="radio" name="selectedColumnInfo" value="<%=c.getColumnNumber()%>:<%=c.getId()%>"></td>
													<td class="dataCellNumerical"><%= c.getColumnNumber() %></td>
													<td class="dataCellNumerical"><%= c.getId() %></td>
													
												
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getLabel()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getFieldId()) %></td>
													
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getPropertyType()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getPropertyName()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getIsPreferred()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getRepresentationalForm()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getSource()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getQualifierName()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getQualifierValue()) %></td>
													<td class="dataCellText"><%= StringUtils.getSpaceIfBlank(c.getDelimiter()) %></td>
													<td class="dataCellNumerical"><%= c.getConditionalColumnId() %></td>
													
												</tr>
										<% 
													} 
												    }
												}

											   } catch(Exception e) {
												  e.printStackTrace();
											   }							
			   
										%>	

									</table>
								</td>
							</tr>
							<tr>
								<td align="right" class="actionSection">
									<!-- bottom action buttons begins -->
									<table cellpadding="4" cellspacing="0" border="0">
										<tr>
										
					<%					
					if (objs.length == -1) {
					%>
											<td><h:commandButton id="insertbefore" action="#{userSessionBean.insertBeforeColumnAction}" value="InsertBefore" /></td>
					                                                <td><h:commandButton id="insertafter" action="#{userSessionBean.insertAfterColumnAction}" value="InsertAfter" /></td>
                                        <%
                                        } else {
                                        %>
 											<td><h:commandButton id="insertbefore" action="#{userSessionBean.insertBeforeColumnAction}" value="Add" /></td>
                                        <%
                                        }
                                        %>
                                        
											<td><h:commandButton id="modify" action="#{userSessionBean.modifyColumnAction}" value="Modify" /></td>
											
											
											<td><h:commandButton id="delete" action="#{userSessionBean.deleteColumnAction}" onclick="if (!confirm('You will lose all data entered. Are you sure?')) return false" value="Delete" /></td>
											
											
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
    
        <%@ include file="/pages/templates/nci_footer.jsp" %>
    
       </td>
  </tr>
</table>

</f:view>
</body>
</html>

