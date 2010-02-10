<%@ page import="java.util.*" %>

<%
  String[] problems = new String[] { 
   "I am locked out of my account", 
   "I forgot my password",
   "My password does not work",
  }; 
%>

What's the problem you are experiencing?<br/>
<%
  for (int i=0; i<problems.length; ++i) {
%>
    <input type="radio" name="request" value="<%=problems[i]%>"><%=problems[i]%><br/>
<%
  }
%>
Login ID: <input type="text" name="userid"><br/>
