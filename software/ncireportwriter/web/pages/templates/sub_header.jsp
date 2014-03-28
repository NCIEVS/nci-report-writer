<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ page import="gov.nih.nci.evs.utils.*" %>
<%
  String imagesPath = FormUtils.getImagesPath(request);
%>
<div>
  <a href="http://evs.nci.nih.gov/" target="_blank">
    <img src="<%=imagesPath%>/evs-logo.gif" width="945" height="26"
      alt="Enterprise Vocabulary Services" border="0"/>
  </a>
</div>
