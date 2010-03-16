<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="gov.nih.nci.evs.reportwriter.webapp.*" %>
<%@ page import="gov.nih.nci.evs.utils.*" %>
<%!
  private static final String TELEPHONE = "301.451.4384 or Toll-Free: 888.478.4423";
  private static final String MAIL_TO = "ncicb@pop.nci.nih.gov";
  private static final String NCICB_URL = "http://ncicb.nci.nih.gov/support";
  
  // List of attribute name(s):
  private static final String SUBJECT = ContactUsRequest.SUBJECT;
  private static final String EMAIL_MSG = ContactUsRequest.EMAIL_MSG;
  private static final String EMAIL_ADDRESS = ContactUsRequest.EMAIL_ADDRESS;
%>
<%
  String subject = HTTPUtils.getJspAttributeString(request, SUBJECT);
  String email_msg = HTTPUtils.getJspAttributeString(request, EMAIL_MSG);
  String email_address = HTTPUtils.getJspAttributeString(request, EMAIL_ADDRESS);
  String warningMsg = (String) request.getAttribute("warningMsg");
  boolean hasWarningMsg = warningMsg != null && warningMsg.trim().length() > 0;
  String infoMsg = (String) request.getAttribute("infoMsg");
%>
<f:view>
  <div class="welcomeTitle" style="height: 15px">Contact Us</div>
  <hr/>

  <div>
    <b>You can request help or make suggestions by filling out the
      online form below, or by using any one of these contact points:
    </b>
  </div>

  <table>
    <tr><td height="15px"></td></tr>
    <tr>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td>Telephone:</td>
      <td><%=TELEPHONE%></td>
    </tr>
    <tr>
      <td/>
      <td>Email:</td>
      <td><a href="mailto:<%=MAIL_TO%>"><%=MAIL_TO%></a></td>
    </tr>
    <tr>
      <td/>
      <td>Web Page:</td>
      <td><a href="<%=NCICB_URL%>" target="_blank"><%=NCICB_URL%></a></td>
    </tr>
  </table>
  <br/>

  <div>
    Telephone support is available Monday to Friday, 8 am - 8 pm 
    Eastern Time, excluding government holidays. You may leave a 
    message, send an email, or submit a support request via the Web
    at any time.  Please include: 
    <ul>
      <li>Your contact information;</li>
      <li>Reference to the Term Suggestions Application; and</li>
      <li>A detailed description of your problem or suggestion.</li>
    </ul>

    For questions related to NCI's Cancer.gov Web site,
    see the
    <a href="http://www.cancer.gov/help" target="_blank">
      Cancer.gov help page</a>. &nbsp;
    For help and other questions concerning NCI Enterprise Vocabulary
    Services (EVS),
    see the <a href="http://evs.nci.nih.gov/" target="_blank">
      EVS Web site</a>.
  </div>

  <%
    String color = ""; 
    if (hasWarningMsg)
      color = "style=\"color:#FF0000;\"";
  %>
  <br/><br/><b>Online Form</b>
  <br/>
  <p <%= color %>>
    To use this web form, please fill in every box below and then click on 'Submit'. 
    <%
      if (hasWarningMsg) {
    %>
        <br/><i style="color:#FF0000;"><%= StringUtils.toHtml(warningMsg) %></i>
    <%
      }
      if (infoMsg != null && infoMsg.trim().length() > 0) {
    %>
        <br/><i class="fyiMsgColor"><%= StringUtils.toHtml(infoMsg) %></i>
    <%
      }
    %>
  </p>
  
  <form method="post">
    <% if (hasWarningMsg) %> <i style="color:#FF0000;">* Required)</i>
    <i>Subject of your email:</i>
    <br/>
    <input class="inputItem" style="width: 690px" name="subject" alt="Subject" value="<%= subject %>" onFocus="active = true" onBlur="active = false" onKeyPress="return ifenter(event,this.form)">
    <br/><br/>
    <% if (hasWarningMsg) %> <i style="color:#FF0000;">* Required)</i>
    <i>Detailed description of your problem or suggestion (no attachments):</i>
    <br/>
    <textarea class="textItem" style="width: 690px" rows="4" Name="<%= EMAIL_MSG %>" rows="4"><%= email_msg %></textarea>
    <br/><br/>
    <% if (hasWarningMsg) %> <i style="color:#FF0000;">* Required)</i>
    <i>Your e-mail address:</i>
    <br/>
    <input class="inputItem" style="width: 690px" name="<%= EMAIL_ADDRESS %>" alt="<%= EMAIL_ADDRESS %>" value="<%= email_address %>" onFocus="active = true" onBlur="active = false" onKeyPress="return ifenter(event,this.form)">
    <br/><br/>
    
    <h:commandButton
      id="submit"
      value="Submit"
      action="#{userSessionBean.submitContactUs}"
      alt="submit" />
    <h:commandButton
      id="clear"
      value="Clear"
      action="#{userSessionBean.clearContactUs}"
      alt="clear" />
    <h:commandButton 
      id="back" 
      value="Back"
      action="back"
      alt="back" />
  </form>
  <br/>
  
  <a href="http://www.cancer.gov/policies/page3" target="_blank">
    <i>Privacy Policy on E-mail Messages Sent to the NCI Web Site</i>
  </a>
  
</f:view>
