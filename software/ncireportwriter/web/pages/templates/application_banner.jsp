<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

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
