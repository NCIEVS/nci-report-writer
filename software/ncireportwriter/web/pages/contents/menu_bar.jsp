<!-- Menu Bar (Begin) -->
<table summary="" cellpadding="0" cellspacing="0" border="0" height="20">
  <tr>
    <td width="1">
      <!-- anchor to skip main menu -->
      <a href="#content">
        <img src="<%= request.getContextPath() %>/images/shim.gif" 
            alt="Skip Menu" width="1" height="1" border="0" />
      </a>
    </td>
    
    <td height="20" class="mainMenuItemOver" 
        onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" 
        onmouseout="changeMenuStyle(this,'mainMenuItemOver'),hideCursor()" 
        onclick="document.location.href='<%= request.getContextPath() %>/pages/main/task.jsf'">
      <a class="mainMenuLink" href="<%= request.getContextPath() %>/pages/main/task.jsf">MAIN</a>
    </td>

    <td>
      <img src="<%= request.getContextPath() %>/images/mainMenuSeparator.gif" 
          width="1" height="16" alt="" />
    </td>
    
    <!-- link 5 begins -->
    <td height="20" class="mainMenuItem" 
        onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" 
        onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" >
      <a class="mainMenuLink" href="<%= request.getContextPath() %>/pages/logout.jsf">LOGOUT</a>
   
    </td>

    <td>
      <img src="<%= request.getContextPath() %>/images/mainMenuSeparator.gif" 
        width="1" height="16" alt="" />
    </td>
  </tr>
</table>
<!-- Menu Bar (End) -->