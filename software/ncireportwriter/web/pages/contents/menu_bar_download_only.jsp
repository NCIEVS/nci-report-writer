<!-- Menu Bar Download Only (Begin) -->
<table summary="" cellpadding="0" cellspacing="0" border="0" height="20">
  <tr>
    <td width="1">
      <!-- anchor to skip main menu -->
      <a href="#content">
        <img src="<%= request.getContextPath() %>/images/shim.gif" 
            alt="Skip Menu" width="1" height="1" border="0" />
      </a>
    </td>

    <td height="20" class="mainMenuItem" 
        onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" 
        onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()">
      <%-- <a class="mainMenuLink" href="<%= request.getContextPath() %>/pages/main/download_nologin.jsf">DOWNLOAD</a> --%>
      <a class="mainMenuLink" href="#" 
          onclick="javascript:window.open('<%= request.getContextPath() %>/pages/main/download_nologin.jsf', '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">DOWNLOAD REPORTS</a>
    </td>

    <td>
      <img src="<%= request.getContextPath() %>/images/mainMenuSeparator.gif" 
          width="1" height="16" alt="" />
    </td>
</table>
<!-- Menu Bar Download Only (End) -->