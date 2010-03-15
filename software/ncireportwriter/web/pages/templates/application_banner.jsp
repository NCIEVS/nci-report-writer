<%@ page import="gov.nih.nci.evs.utils.*" %>
<%
  String basePath = FormUtils.getBasePath(request);
  String imagesPath = FormUtils.getImagesPath(request);
%>

<div class="subhdrBG">
  <a href="<%=basePath%>">
    <img src="<%=imagesPath%>/appLogo.gif"
      alt="Application Logo" height="97" border="0">
  </a>
</div>
