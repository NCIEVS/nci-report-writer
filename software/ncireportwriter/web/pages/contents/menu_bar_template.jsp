<%
  String menu_bar = request.getParameter("menu_bar");
%>
<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
  <tr>
    <td valign="top" width="100%">
      <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
      
        <% if (menu_bar != null && menu_bar.equals("/pages/contents/menu_bar_skip.jsp")) { %>
          <tr>
            <td height="20" width="100%" class="mainMenu">
              <%@ include file="/pages/contents/menu_bar_skip.jsp" %>
            </td>
          </tr>
        <% } else if (menu_bar != null && menu_bar.equals("/pages/contents/menu_bar.jsp")) { %>
          <tr>
            <td height="20" width="100%" class="mainMenu">
              <%@ include file="/pages/contents/menu_bar.jsp" %>
            </td>
          </tr>
        <% } %>

        <tr>
          <td width="100%" valign="top">
            <jsp:include page="<%=request.getParameter("content_page")%>"/>
          </td>
        </tr>
            
      </table>
    </td>
  </tr>
</table>