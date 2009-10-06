<%
  String queryString = request.getQueryString();
  boolean logout = queryString != null && queryString.equals("logout");
%>
<html>
 <body>
  <jsp:forward page="/pages/templates/template.jsp">
    <jsp:param name="menu_bar" value="/pages/contents/menu_bar_skip.jsp"/>
    <jsp:param name="content_page" value="/pages/contents/_login.jsp"/>
    <jsp:param name="logout" value="<%=logout%>"/>
  </jsp:forward>
 </body>
</html>
