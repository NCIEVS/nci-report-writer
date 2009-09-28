<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%
  String imagesPath = FormUtils.getImagesPath(request);
%>
<div class="ncibanner">
  <a href="http://www.cancer.gov" target="_blank"
     alt="National Cancer Institute" >
    <img src="<%=imagesPath%>/logotype.gif"
      <%-- width="440" height="39" --%> border="0"
      alt="National Cancer Institute" />
  </a>
  <a href="http://www.cancer.gov" target="_blank"
     alt="National Cancer Institute" >
    <img src="<%=imagesPath%>/spacer.gif"
      width="192" height="39" border="0" 
      alt="National Cancer Institute" class="print-header" /></a>
  <a href="http://www.nih.gov" target="_blank"
      alt="U.S. National Institutes of Health">
    <img src="<%=imagesPath%>/tagline_nologo.gif"
      width="173" height="39" border="0"
      alt="U.S. National Institutes of Health" />
  </a>
  <a href="http://www.cancer.gov" target="_blank"
      alt="www.cancer.gov">
    <img src="<%=imagesPath%>/cancer-gov.gif"
      width="99" height="39" border="0"
      alt="www.cancer.gov" />
  </a>
</div>
