<!-- Menu Bar (Begin) -->
<table summary="" cellpadding="0" cellspacing="0" border="0" height="20">
  <tr bgcolor="#5C5C5C">
    <td height="20" class="mainMenuItemOver">
      <a class="mainMenuLink" href="<%= request.getContextPath() %>/pages/main/task.jsf">MAIN</a>
    </td>

    <td>
      <img src="<%= request.getContextPath() %>/images/mainMenuSeparator.gif" 
          width="1" height="16" alt="" />
    </td>

    <td height="20" class="mainMenuItem">
      <a class="mainMenuLink" href="#" 
          onclick="javascript:window.open('<%= request.getContextPath() %>/pages/main/download_nologin.jsf', '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">DOWNLOAD REPORTS</a>
    </td>

    <td>
      <img src="<%= request.getContextPath() %>/images/mainMenuSeparator.gif" 
          width="1" height="16" alt="" />
    </td>

    <td height="20" class="mainMenuItem" align="right">
      <a class="mainMenuLink" href="<%= request.getContextPath() %>/pages/logout.jsf">LOGOUT</a>
    </td>

    <td>
      <img src="<%= request.getContextPath() %>/images/mainMenuSeparator.gif" 
        width="1" height="16" alt="" />
    </td>
    
    <td width="100%"/>
  </tr>
</table>
<!-- Menu Bar (End) -->