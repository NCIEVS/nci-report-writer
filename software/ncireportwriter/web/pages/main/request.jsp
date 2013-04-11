<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
L--%>

<%@ taglib uri="EVSUIComponents" prefix="ui" %>
<ui:insert template="/pages/templates/template.jsp">
  <ui:put name="menu_bar" url="/pages/contents/menu_bar_download_only.jsp"/>
  <ui:put name="content_page" url="/pages/contents/_request.jsp"/>
</ui:insert>