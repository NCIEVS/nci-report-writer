<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%
  String basePath = FormUtils.getBasePath(request);
%>
<div class="bluebar">
  <div id="quicklinksholder">
    <ul id="quicklinks"
        onmouseover="document.quicklinksimg.src='<%=basePath%>/images/quicklinks-active.gif';"
        onmouseout="document.quicklinksimg.src='<%=basePath%>/images/quicklinks-inactive.gif';">
      <li>
        <a href="#">
          <img src="<%=basePath%>/images/quicklinks-inactive.gif" width="162"
            height="18" border="0" name="quicklinksimg" alt="Quick Links" />
        </a>
        <ul>
<%-- From NCIt Browser (Begin)        
          <li><a href="http://evs.nci.nih.gov/" target="_blank"
            alt="Enterprise Vocabulary Services">EVS Home</a></li>
          <li><a href="http://ncimeta.nci.nih.gov/MetaServlet/" target="_blank"
            alt="NCI MetaThesaurus">NCI MetaThesaurus</a></li>
          <li><a href="http://bioportal.nci.nih.gov" target="_blank"
            alt="NCI Bioportal">NCI Bioportal</a></li>
          <li><a href="http://www.cancer.gov/cancertopics/terminologyresources" target="_blank"
            alt="NCI Terminology Resources">NCI Terminology Resources</a></li>
--%> <%-- From NCIt Browser (End) --%>

          <li><a href="http://www.cancer.gov" target="_blank"
            alt="Enterprise Vocabulary Services">National Cancer Institute</a></li>
          <li><a href="http://ncicb.nci.nih.gov/" target="_blank"
            alt="Enterprise Vocabulary Services">NCI Center for Bioinformatics</a></li>
          <li><a href="http://gforge.nci.nih.gov/projects/reportwriter/" target="_blank"
            alt="Enterprise Vocabulary Services">Project GForge Site</a></li>
          <li><a href="https://cabig.nci.nih.gov/help" target="_blank"
            alt="Enterprise Vocabulary Services">NCICB Application Support</a></li>
          <li><a href="http://bioportal.nci.nih.gov" target="_blank"
            alt="Enterprise Vocabulary Services">NCI Bioportal</a></li>
            
        </ul>        
      </li>
    </ul>
  </div>
</div>
