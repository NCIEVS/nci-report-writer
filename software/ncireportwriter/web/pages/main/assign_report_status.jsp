<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>


<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.bean.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.utils.*" %>
<%@ page import="gov.nih.nci.evs.utils.*" %>
<%@ page import="gov.nih.nci.evs.reportwriter.properties.*" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
  private static final String buildInfo = AppProperties.getInstance().
    getSpecialProperty(AppProperties.BUILD_INFO);
  private static final String application_version = AppProperties.getInstance().
    getSpecialProperty(AppProperties.APPLICATION_VERSION);
  private static final String anthill_build_tag_built = AppProperties.getInstance().
    getSpecialProperty(AppProperties.ANTHILL_BUILD_TAG_BUILT);
  private static final String evs_service_url = AppProperties.getInstance().
    getSpecialProperty(AppProperties.EVS_SERVICE_URL);
%>

<%
  String imagesPath = FormUtils.getImagesPath(request);
%>
<!--
   Build info: <%=buildInfo%>
 Version info: <%=application_version%>
          Tag: <%=anthill_build_tag_built%>
   LexEVS URL: <%=evs_service_url%>          
  -->
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><ui:get name="title" default="NCI Report Writer"/></title>
    <link rel="stylesheet" type="text/css" href="<%= FormUtils.getCSSPath(request) %>/styleSheet.css" />
    <link rel="shortcut icon" href="<%= FormUtils.getBasePath(request) %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  </head>
  <body>
    <%@ include file="/pages/templates/header.jsp"%>
    <div class="center-page">
     <%@ include file="/pages/templates/sub_header.jsp"%>
     <div class="mainbox-top"><img src="<%=imagesPath%>/mainbox-top.gif"
        width="947" height="5" alt="Mainbox Top" /></div>
      <div id="main-area">
        <%@ include file="/pages/templates/application_banner.jsp"%>
        <%@ include file="/pages/templates/quick_links.jsp"%>
        <div class="pagecontent">
          <%@ include file="/pages/contents/menu_bar.jsp"%>
         
          <%@ include file="/pages/contents/_assign_report_status.jsp"%>
          
          
          <%@ include file="/pages/templates/footer.jsp"%>
        </div>
      </div>
      <!-- Note: Keep the div below on one line to avoid IE bug. -->
      <div class="mainbox-bottom"><img src="<%=imagesPath%>/mainbox-bottom.gif"
        width="947" height="5" alt="Mainbox Bottom" /></div>
    </div>
    <br/>
  </body>
</html>


