<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<%@ page import="gov.nih.nci.evs.utils.*" %>

<%
String css = WebUtils.isUsingIE(request) ? "_IE" : "";
String author = "";
String keywords = "";
String title = "";
String subject = "";
String frozen_rows = "";
%>

<f:view>
  <h:form id="ADMINISTER_EXCEL_METADATAForm">
 
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0">
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">ADMINISTER EXCEL METADATA</td>
                  </tr>
                  <tr>
                    <td>
                    
                    
                    
                      <table summary="Enter metadata here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%">
                        <tr class="dataRowLight">
                          <td class="dataCellText">
                            <h:outputText value="Report" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="standardReportTemplateId" 
                                value="#{userSessionBean.selectedStandardReportTemplate_draft}" 
                                valueChangeListener="#{userSessionBean.reportSelectionChanged_draft}">
                              <f:selectItems value="#{userSessionBean.standardReportTemplateList_draft}"/>
                            </h:selectOneMenu>
                          </td>
                        </tr>
                        
                        
			  <tr>
			    <td class="requestLabel">Author: <i
			      class="warningMsgColor">*</i></td>
			    <td><input class="requestPageTF<%=css%>"
			      name="author" alt="author" value="<%=author%>"></td>
			  </tr>
			  
			  <tr>
			    <td class="requestLabel">Keywords: <i
			      class="warningMsgColor">*</i></td>
			    <td><input class="requestPageTF<%=css%>"
			      name="keywords" alt="keywords" value="<%=keywords%>"></td>
			  </tr>
			  <tr>
			    <td class="requestLabel">Title:</td>
			    <td><input class="requestPageTA<%=css%>"
			      name="title" alt="title" value="<%=title%>"></td>
			  </tr>
			  <tr>
			    <td class="requestLabel">Subject<br/>
			    Information:</td>
			    <td><textarea class="requestPageTA<%=css%>"
			      name="subject"><%=subject%></textarea></td>
			  </tr>	

			  <tr>
			    <td class="requestLabel">Number of Frozen Header Rows: <br/>
			    Information:</td>
			    <td><input class="requestPageTA<%=css%>"
			      name="frozen_rows" alt="frozen_rows" value="<%=frozen_rows%>"></td>
			  </tr>	
			  
			  <tr>
			    <td class="requestLabel"><i class="warningMsgColor">*
			    Required</i><br/><br/></td>
			  </tr>                        
                                    
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="save" value="Save" 
                              action="#{userSessionBean.assignStatusAction}" /></td>
                          <td><h:commandButton id="back" action="back" value="Back" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table> <!-- Table 2 (End) -->
        </td>
      </tr>
    </table> <!-- Table 1 (End) -->
  </h:form>
</f:view>