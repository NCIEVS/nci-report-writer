<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%@ page import="gov.nih.nci.evs.utils.*" %>

<%
  OntologyBean ontologyBean = BeanUtils.getOntologyBean();
  String label = "";
  String rootConceptCode = "";
  Boolean direction = Boolean.FALSE;
  boolean enabledSave = true;
  boolean firstTime = request.getAttribute("ValueChangeEvent") == null;
  
  if (firstTime) {
    ontologyBean.setDefaultSelectedOntology();
  }

  String warningMsg = (String) request.getAttribute("warningMsg");
  if (warningMsg != null && warningMsg.trim().length() > 0) {
    label = (String) request.getAttribute("label");
    ontologyBean.setSelectedOntology((String) request.getAttribute("selectedOntology"));
    rootConceptCode = (String) request.getAttribute("rootConceptCode");
    direction = (Boolean) request.getAttribute("direction");
  }
  
  StringBuffer warningBuffer = new StringBuffer();
  if (! RemoteServerUtil.isRunning(warningBuffer)) {
    if (warningMsg != null && warningMsg.trim().length() > 0)
        warningBuffer.append("\n" + warningMsg);
    warningMsg = warningBuffer.toString();
    enabledSave = false;
  }
%>

<f:view>
  <h:form id="SELECT_TASKForm">
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
                    <td class="dataTablePrimaryLabel" height="20">CREATE REPORT TEMPLATE</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" class="dataTable" 
                          cellpadding="3" cellspacing="0" border="0" width="100%">
                          <!-- Table 4 (Begin) -->

                        <tr class="dataRowDark">
                          <td class="dataCellText">Label</td>
                          <td class="dataCellText"><input type="text" name="label" value="<%=label%>"></td>
                        </tr>
                        
                        <tr class="dataRowLight">
                          <td class="dataCellText">                                           
                            <h:outputText value="Coding Scheme:" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="ontologyId" 
                              value="#{ontologyBean.selectedOntology}" onchange="submit();"
                              valueChangeListener="#{ontologyBean.ontologySelectionChanged}" >
                                <f:selectItems value="#{ontologyBean.ontologyList}" />
                            </h:selectOneMenu>                                          
                          </td>                                           
                        </tr>
                        
                        <tr class="dataRowDark">
                          <td class="dataCellText">Root Concept Code</td>
                          <td class="dataCellText"><input type="text" name="rootConceptCode" value="<%=rootConceptCode%>"></td>
                        </tr>

                        <tr class="dataRowLight">
                          <td class="dataCellText">                                           
                            <h:outputText value="Association Name" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="associationId" 
                                value="#{ontologyBean.selectedAssociation}" 
                                valueChangeListener="#{ontologyBean.associationSelectionChanged}" >
                              <f:selectItems value="#{ontologyBean.associationList}" />
                            </h:selectOneMenu>                                          
                          </td>                                           
                        </tr>
                        
                        <tr class="dataRowDark">
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
                        
                        <tr class="dataRowLight">
                          <td class="dataCellText">
                            <h:outputText value="Level" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="LevelId" value="#{ontologyBean.selectedLevel}" 
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
                                action="#{userSessionBean.saveTemplateAction}" /></td>
                          <% } else {%>
                            <td><h:commandButton id="save" value="Save" disabled="true"
                                action="#{userSessionBean.saveTemplateAction}" /></td>
                          <% } %>
                          <% String enabledState = enabledSave ? "" : "disabled=\"disabled\""; %>
                          <td><input type="reset" value="Reset" <%=enabledState%>/></td>
                          <td><h:commandButton id="back" value="Back" action="back" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table> <!-- Table 3 (End) -->
              </td>
            </tr>
          </table> <!-- Table 2 (End) -->
        </td>
      </tr>
            
    </table> <!-- Table 1 (End) -->
  </h:form>
</f:view>