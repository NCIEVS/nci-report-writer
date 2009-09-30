<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:view>
  <h:form id="SELECT_TASKForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%">
        
      <tr>
        <td height="20" width="100%" class="mainMenu">
          <%@ include file="/pages/contents/menuBar.jsp" %>
        </td>
      </tr>

      <tr>
        <td height="500" width="100%" valign="top"><br>
          <!-- target of anchor to skip menus -->
          <a name="content" />
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              width="600" class="contentPage">
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                      <td class="dataTablePrimaryLabel" height="20">SELECT TASK</td>
                    </tr>
                    <tr>
                      <td>
                        <table summary="Enter summary of data here" cellpadding="3" 
                            cellspacing="0" border="0" class="dataTable" width="100%">
                          <tr class="dataRowLight">
                            <td class="dataCellText">
                              <h:selectOneMenu id="id" value="#{userSessionBean.selectedTask}" 
                                  valueChangeListener="#{userSessionBean.taskSelectionChanged}" >
                                <f:selectItems value="#{userSessionBean.taskList}"/>
                              </h:selectOneMenu>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td align="right" class="actionSection">
                        <table cellpadding="4" cellspacing="0" border="0">
                          <tr>
                            <td><h:commandButton id="next" action="#{userSessionBean.performTask}" value="Next" /></td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </h:form>
</f:view>