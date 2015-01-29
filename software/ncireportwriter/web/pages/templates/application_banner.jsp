<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ page import="gov.nih.nci.evs.utils.*" %>
<%
  String bannerBasePath = FormUtils.getBasePath(request);
  String bannerImagesPath = FormUtils.getImagesPath(request);
%>

<div class="subhdrBG">
  <a href="<%=bannerBasePath%>">
    <img src="<%=bannerImagesPath%>/appLogo.gif"
      alt="Application Logo" height="97" border="0">
  </a>
</div>
