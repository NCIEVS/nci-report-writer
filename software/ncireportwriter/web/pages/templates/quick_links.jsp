<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ page import="gov.nih.nci.evs.utils.*" %>
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
          <li><a href="http://www.cancer.gov"
            target="_blank">National Cancer Institute</a></li>
          <li><a href="http://ncicb.nci.nih.gov/"
            target="_blank">NCI Center for Bioinformatics</a></li>
          <li><a href="http://gforge.nci.nih.gov/projects/reportwriter/"
            target="_blank">Project GForge Site</a></li>
          <li><a href="https://cabig.nci.nih.gov/help"
            target="_blank">NCICB Application Support</a></li>
          <li><a href="http://nciterms51-dev.nci.nih.gov/"
            target="_blank">NCI Term Browser</a></li>
        </ul>        
      </li>
    </ul>
  </div>
</div>
