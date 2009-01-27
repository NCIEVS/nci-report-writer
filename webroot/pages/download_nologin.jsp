<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources" var="reportwriterBundle"/>

<%@ page contentType="text/html;charset=windows-1252"%>

<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>


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
<h:form id="AVAILABLE_STANDARD_REPORTSForm">
	<tr>
		<td width="100%" valign="top"><br>
		<!-- target of anchor to skip menus --><a name="content" />
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="dataTablePrimaryLabel" height="20">STANDARD REPORTS</td>
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
										<tr>
											<th class="dataTableHeader" scope="col" align="center">Report Label</th>
											<th class="dataTableHeader" scope="col" align="center">File Name</th>
											<th class="dataTableHeader" scope="col" align="center">Format</th>
											<th class="dataTableHeader" scope="col" align="center">Coding Scheme</th>
											<th class="dataTableHeader" scope="col" align="center">Version</th>
											<th class="dataTableHeader" scope="col" align="center">Created By</th>
											<th class="dataTableHeader" scope="col" align="center">Last Modified</th>
											<th class="dataTableHeader" scope="col" align="center">Status</th>
										</tr>

<%
        try{
            SDKClientUtil sdkclientutil = new SDKClientUtil();
	    StandardReportTemplate standardReportTemplate = null;
	    String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
	    Object[] objs = sdkclientutil.search(FQName);
	    int n = 0;
	    if (objs != null && objs.length > 0) {
		    for (int i=0; i<objs.length; i++)
		    {
			    StandardReport standardReport = (StandardReport) objs[i];
			    ReportFormat reportFormat = standardReport.getFormat();
			    ReportStatus reportStatus = standardReport.getStatus();
			    standardReportTemplate = standardReport.getTemplate();
			    if (reportFormat != null && standardReportTemplate != null && reportStatus != null)
			    {
			          String label = standardReportTemplate.getLabel();
				  String format = reportFormat.getDescription();
				  String codingScheme = standardReportTemplate.getCodingSchemeName();
				  String version = standardReportTemplate.getCodingSchemeVersion();
				  Date lastModified = standardReport.getLastModified();
				  String status = reportStatus.getLabel();
				  
				  String pathname = standardReport.getPathName();
				  String filename = DataUtils.getFileName(pathname);
				  
				  gov.nih.nci.evs.reportwriter.bean.User user = standardReport.getCreatedBy();
				  String loginName = user.getLoginName();
				  
				  String date_str = null;
				  if (lastModified != null)
				  {
				      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				      date_str = formatter.format(lastModified);
				  }
				  if (n % 2 == 0)
				  {
				  %>
				      <tr class="dataRowLight">
				  <%    
				  }
				  else
				  {
				  %>
				      <tr class="dataRowDark"> 
				  <%    
				  }
				  
				  Integer id = standardReportTemplate.getId();
				  String templateId = id.toString();
				  
				  Integer format_id = reportFormat.getId();
				  String formatId = format_id.toString();


%>
<td class="dataCellText"><a href="<%=request.getContextPath() %>/fileServlet?template=<%=templateId%>&format=<%=formatId%>" ><%=label%></a></td>
										
											<td class="dataCellText"><%=filename%></td>
											<td class="dataCellText"><%=format%></td>
											<td class="dataCellText"><%=codingScheme%></td>
											<td class="dataCellText"><%=version%></td>
											<td class="dataCellText"><%=loginName%></td>
											<td class="dataCellText"><%=date_str%></td>
											<td class="dataCellText"><%=status%></td>
										</tr>
										
										
<%
                                  n++; 
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
											<td><h:commandButton id="back" action="back" value="Back"  /></td>
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

