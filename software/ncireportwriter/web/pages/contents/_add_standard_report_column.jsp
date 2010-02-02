<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>

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
  String warning = (String) request.getAttribute("warningMsg");
%>

<f:view>
  <h:form id="ADD_STANDARD_REPORT_FIELDForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->
      <tr>
        <td height="500" width="100%" valign="top">
          <br>
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="725" class="contentPage"> <!-- Table 2 (Begin) -->
            <% if (warning != null) { %>
              <tr><td class="warningMsgColor">
                Warning:<br/>
                <%=StringUtils.toHtml(warning)%><br/>
                <br/>
              </td></tr>
            <% } %>
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0"> <!-- Table 3 (Begin) -->
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">ADD REPORT FIELD</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" 
                          cellpadding="3" cellspacing="0" border="0" 
                          class="dataTable" width="100%"> <!-- Table 4 (Begin) -->

                        <tr class="dataRowLight">
                          <td class="dataCellText">Column Number</td>
                          <td class="dataCellText">
                            <input type="text" name="columnNumber" value="<%=columnNumber%>">
                          </td>
                        </tr>

                        <tr class="dataRowDark">
                          <td class="dataCellText">Field Label</td>
                          <td class="dataCellText">
                            <input type="text" name="fieldlabel" value="<%=fieldlabel%>">
                          </td>
                        </tr>

                        <tr class="dataRowLight">
                          <td class="dataCellText">Field Type</td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="DataCategoryId" 
                                value="#{ontologyBean.selectedDataCategory}" 
                                valueChangeListener="#{ontologyBean.dataCategorySelectionChanged}" >
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
                            <h:selectOneMenu id="PropertyNameId" 
                                value="#{ontologyBean.selectedPropertyName}" 
                                valueChangeListener="#{ontologyBean.propertyNameSelectionChanged}" >
                              <f:selectItems value="#{ontologyBean.propertyNameList}" />
                            </h:selectOneMenu>
                          </td>                                               
                        </tr>

                        <tr class="dataRowDark">
                          <td class="dataCellText">Is Preferred?</td>
                          <td class="dataCellText">
                            <input type="radio" name="preferred" value="yes" >Yes&nbsp;
                            <input type="radio" name="preferred" value="no">No&nbsp;
                            <input type="radio" name="preferred" value="na" checked="checked">NA
                          </td>
                        </tr>

                        <tr class="dataRowLight">
                          <td class="dataCellText">Representational Form</td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="RepresentationalFormId" 
                                value="#{ontologyBean.selectedRepresentationalForm}" 
                                valueChangeListener="#{ontologyBean.representationalFormSelectionChanged}" >
                              <f:selectItems value="#{ontologyBean.representationalFormList}" />
                            </h:selectOneMenu>
                          </td>
                        </tr>

                        <tr class="dataRowDark">
                          <td class="dataCellText">Source</td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="SourceId" 
                                value="#{ontologyBean.selectedSource}" 
                                valueChangeListener="#{ontologyBean.sourceSelectionChanged}" >
                              <f:selectItems value="#{ontologyBean.sourceList}" />
                            </h:selectOneMenu>
                          </td>                   
                        </tr>

                        <tr class="dataRowLight">
                          <td class="dataCellText">Property Qualifier</td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="PropertyQualifierId" 
                                value="#{ontologyBean.selectedPropertyQualifier}" 
                                valueChangeListener="#{ontologyBean.propertyQualifierSelectionChanged}" >
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
                            <h:selectOneMenu id="DelimiterId" 
                                value="#{ontologyBean.selectedDelimiter}" 
                                valueChangeListener="#{ontologyBean.delimiterSelectionChanged}" >
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

                      </table> <!-- Table 4 (End) -->
                    </td>
                  </tr>
                  
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="save" value="Save" 
                              action="#{userSessionBean.saveReportColumnAction}" /></td>
                          <td><input type="reset" value="Reset" /></td>
                          <td><h:commandButton id="back" action="back" value="Back" /></td>
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