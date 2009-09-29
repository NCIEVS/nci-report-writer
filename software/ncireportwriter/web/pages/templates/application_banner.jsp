<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%
  String basePath = FormUtils.getBasePath(request);
  String imagesPath = FormUtils.getImagesPath(request);
%>

<div class="subhdrBG">
  <a href="<%=basePath%>">
    <img src="<%=imagesPath%>/appLogo.gif" 
      alt="Application Logo" height="35" hspace="10" border="0">
  </a>
</div>
