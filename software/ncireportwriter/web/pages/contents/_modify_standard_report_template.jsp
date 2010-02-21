<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%@ page import="org.LexGrid.codingSchemes.*" %>

<%
  StandardReportTemplate standardReportTemplate = null;
  String label = null;
  String codingSchemeName = null;
  String version = null;
  String rootcode = null;
  String associationname = null;
  Boolean direction = null;
  String level = null;
  boolean enabledSave = true;
  
  try {
    String templateLabel = (String) request.getSession().getAttribute("selectedStandardReportTemplate");
    SDKClientUtil sdkclientutil = new SDKClientUtil();

    String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
    String methodName = "setLabel";
    String key = templateLabel;
    Object obj = sdkclientutil.search(FQName, methodName, key);

    if (obj != null) {
      standardReportTemplate = (StandardReportTemplate) obj;
      label = standardReportTemplate.getLabel();
      codingSchemeName = standardReportTemplate.getCodingSchemeName();
      version = standardReportTemplate.getCodingSchemeVersion();
      rootcode = standardReportTemplate.getRootConceptCode();
      associationname = standardReportTemplate.getAssociationName();
      direction = standardReportTemplate.getDirection();
      Integer level_obj = standardReportTemplate.getLevel();
      level = level_obj.toString();
    }
    
    String warningMsg = (String) request.getAttribute("warningMsg");
    if (warningMsg != null && warningMsg.trim().length() > 0) {
      // Note: This was caused by errors in validation.
      version = (String) request.getAttribute("version");
      rootcode = (String) request.getAttribute("rootConceptCode");
      direction = (Boolean) request.getAttribute("direction");
    } else {
     // Note: This section is called during the initial page display.
     String csnv = DataUtils.getCSNVKey(codingSchemeName, version);
     String versionTmp = DataUtils.getCodingSchemeVersion(csnv);
     if (versionTmp == null) {
       CodingScheme cs = DataUtils.getCodingScheme(codingSchemeName);
       if (cs != null) {
         versionTmp = cs.getRepresentsVersion();
         String csnvLatest = DataUtils.getCSNVKey(codingSchemeName, versionTmp);
         String msg = "";
         msg += "This report template is referencing an older or invalid version of the coding scheme.\n";
         msg += "Please update the version number to:\n";
         msg += "    * " + versionTmp;
         warningMsg = msg;
       } else {
         warningMsg = codingSchemeName + " " + "coding scheme is currently offline or not loaded.";
         enabledSave = false;
       }
     }
   }

   //Note: Need to help refresh the Association Name list.
   OntologyBean ontologyBean = BeanUtils.getOntologyBean();
   ontologyBean.setSelectedOntology(
     DataUtils.getCSNVKey(codingSchemeName, version));
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
            <% if (warningMsg != null && warningMsg.trim().length() > 0) { %>
              <tr><td class="warningMsgColor">
                Warning:<br/>
                <%=StringUtils.toHtml(warningMsg)%><br/>
                <br/>
              </td></tr>
            <% } %>
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
                                <td class="dataCellText"><%=codingSchemeName%>
                                  <input type="hidden" name="codingScheme" value="<%=codingSchemeName%>">
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
                            <% if (enabledSave) { %>
                              <td><h:commandButton id="save" value="Save" 
                                action="#{userSessionBean.saveModifiedTemplateAction}"/></td>
                            <% } else {%>
                              <td><h:commandButton id="save" value="Save" disabled="true"
                                action="#{userSessionBean.saveModifiedTemplateAction}" /></td>
                            <% } %>
                            <% String enabledState = enabledSave ? "" : "disabled=\"disabled\""; %>
                            <td><input type="reset" value="Reset" <%=enabledState%>/></td>
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
  } catch (Exception e) {
%>
    <%= e.getMessage() %>
<%
    e.printStackTrace();
  }
%>
