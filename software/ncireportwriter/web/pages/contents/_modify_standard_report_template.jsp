<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

<%
  StandardReportTemplate standardReportTemplate = null;
  String label = null;
  String codingScheme = null;
  String version = null;
  String rootcode = null;
  String associationname = null;
  Boolean direction = null;
  String level = null; 
  Object obj = null;
  
  try {
    String templateLabel = (String) request.getSession().getAttribute("selectedStandardReportTemplate");
    SDKClientUtil sdkclientutil = new SDKClientUtil();

    String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
    String methodName = "setLabel";
    String key = templateLabel;

    obj = sdkclientutil.search(FQName, methodName, key);

    if (obj != null) {
      standardReportTemplate = (StandardReportTemplate) obj;
      label = standardReportTemplate.getLabel();
      codingScheme = standardReportTemplate.getCodingSchemeName();
      version = standardReportTemplate.getCodingSchemeVersion();
      rootcode = standardReportTemplate.getRootConceptCode();
      associationname = standardReportTemplate.getAssociationName();
      direction = standardReportTemplate.getDirection();
      Integer level_obj = standardReportTemplate.getLevel();
      level = level_obj.toString();
    }
%>        

<f:view>
  <h:form id="MODIFY_STANDARD_REPORT_TEMPLATEForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0"> <!-- Table 3 (Begin) -->
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">MODIFY REPORT TEMPLATE</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" cellpadding="3" 
                          cellspacing="0" border="0" class="dataTable" width="100%">
                          <!-- Table 4 (Begin) -->
                              <tr class="dataRowDark">
                                <td class="dataCellText">Label</td>
                                <td class="dataCellText"><%=label%></td>
                              </tr>
                              
                              <tr class="dataRowLight">
                                <td class="dataCellText">Coding Scheme</td>
                                <td class="dataCellText"><%=codingScheme%>
                                  <input type="hidden" name="codingScheme" value="<%=codingScheme%>">
                                </td>
                              </tr>
                              
                              <tr class="dataRowDark">
                                <td class="dataCellText">Version</td>
                                <td class="dataCellText">
                                  <input type="text" name="version" value="<%=version%>">
                                </td>
                              </tr>
                              
                              <tr class="dataRowLight">
                                <td class="dataCellText">Root Concept Code</td>
                                <td class="dataCellText">
                                  <input type="text" name="rootConceptCode" value="<%=rootcode%>" >
                                </td>
                              </tr>
                              
                              <tr class="dataRowDark">
                                <td class="dataCellText">                                           
                                  <h:outputText value="Association Name" />
                                </td>
                                <td class="dataCellText">
                                  <h:selectOneMenu id="associationName" 
                                      value="#{ontologyBean.selectedAssociation}" 
                                      valueChangeListener="#{ontologyBean.associationSelectionChanged}" >
                                    <f:selectItems value="#{ontologyBean.associationList}" />
                                  </h:selectOneMenu>                                          
                                </td>                                           
                              </tr>
      
                              <tr class="dataRowLight">
                                <td class="dataCellText">Direction</td>
                                <td class="dataCellText">
                                <% if (direction.equals(Boolean.FALSE)) { %>
                                  <input type="radio" name="direction" value="source" checked="checked">Source&nbsp;
                                  <input type="radio" name="direction" value="target">Target
                                <% } else { %>
                                  <input type="radio" name="direction" value="source" >Source&nbsp;
                                  <input type="radio" name="direction" value="target" checked="checked">Target
                                <% } %>
                               </td>
                              </tr>                                       
                              
                              <tr class="dataRowDark">
                                <td class="dataCellText">
                                  <h:outputText value="Level" />
                                </td>
      
                                <td class="dataCellText">
                                  <h:selectOneMenu id="LevelId" 
                                      value="#{ontologyBean.selectedLevel}" 
                                      valueChangeListener="#{ontologyBean.levelSelectionChanged}" >
                                    <f:selectItems value="#{ontologyBean.levelList}" />
                                  </h:selectOneMenu>
                                </td>
                              </tr>
                        </table> <!-- Table 4 (End) -->
                      </td>
                    </tr>
                    <tr>
                      <td align="right" class="actionSection">
                        <table cellpadding="4" cellspacing="0" border="0">
                          <tr>
                            <td><h:commandButton id="save" action="#{userSessionBean.saveModifiedTemplateAction}" value="Save" /></td>
                            <td><input type="reset" value="Reset" /></td>
                            <td><h:commandButton id="back" action="back" value="Back" /></td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                </table>  <!-- Table 3 (End) -->
              </td>
            </tr>
          </table> <!-- Table 2 (End) -->
        </td> 
      </tr>
    </table> <!-- Table 1 (End) -->
  </h:form>
</f:view>
<%
  } catch (Exception ex) {    
  }
%>
