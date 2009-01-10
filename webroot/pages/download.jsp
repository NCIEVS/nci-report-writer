 <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:loadBundle basename="gov.nih.nci.evs.reportwriter.bean.Resources" var="reportwriterBundle"/>

<%@ page contentType="text/html;charset=windows-1252"%>

<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<%@ page import="java.util.List" %>

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
<h:form id="DOWNLOAD_REPORTForm">


	<tr>
		<td width="100%" valign="top"><br>
		<!-- target of anchor to skip menus --><a name="content" />
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="dataTablePrimaryLabel" height="20">DOWNLOAD REPORT</td>
							</tr>
							<tr>
								<td>
									<table summary="Enter summary of data here" cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%">
<!--

										<tr class="dataRowLight">
											<td class="dataCellText"><a href="<%=request.getContextPath() %>/fileServlet?format=text" ><%=request.getSession().getAttribute("selectedStandardReportTemplate") %> (Tab-delimited)</a></td>
										</tr>


										<tr class="dataRowLight">
											<td class="dataCellText"><a href="<%=request.getContextPath() %>/fileServlet?format=Excel" ><%=request.getSession().getAttribute("selectedStandardReportTemplate") %> (Excel)</a></td>
										</tr>
-->

<%
String formatId = null;
String formatDescription = null;
String template = (String) request.getSession().getAttribute("selectedStandardReportTemplate");
String templateId = null;
try {
	SDKClientUtil sdkclientutil = new SDKClientUtil();
	String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
	String methodName = "setLabel";
	Object obj = sdkclientutil.search(FQName, methodName, template);
	StandardReportTemplate standardReportTemplate = (StandardReportTemplate) obj;
	templateId = (String) DataUtils.int2String(standardReportTemplate.getId());
	
} catch (Exception ex) {

}


List list = DataUtils.getAvailableReportFormat();

if (list != null && list.size() > 0)
{
		for (int i=0; i<list.size(); i++)
		{
			ReportFormat reportFormat = (ReportFormat) list.get(i);
			formatId = (String) DataUtils.int2String(reportFormat.getId());
			formatDescription = reportFormat.getDescription();
%>
			<tr class="dataRowLight">
				<td class="dataCellText"><a href="<%=request.getContextPath() %>/fileServlet?template=<%=templateId%>&format=<%=formatId%>" ><%=template%> (<%=formatDescription%>)</a></td>
			</tr>
<%			
		}
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

