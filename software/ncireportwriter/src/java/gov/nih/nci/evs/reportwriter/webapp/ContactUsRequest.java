package gov.nih.nci.evs.reportwriter.webapp;

import javax.servlet.http.*;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class ContactUsRequest {
    // -------------------------------------------------------------------------
    public static final String SUBJECT = "subject";
    public static final String EMAIL_MSG = "email_msg";
    public static final String EMAIL_ADDRESS = "email_address";
    private boolean _isSendEmail = true;

    // -------------------------------------------------------------------------
    public String clear() {
        HttpServletRequest request = SessionUtils.getRequest();
        request.removeAttribute(SUBJECT);
        request.removeAttribute(EMAIL_MSG);
        request.removeAttribute(EMAIL_ADDRESS);
        return "clear";
    }

    public String submit() {
        HttpServletRequest request = SessionUtils.getRequest();
        AppProperties appProperties = AppProperties.getInstance();
        try {
            String mailServer =
                appProperties.getProperty(AppProperties.MAIL_SMTP_SERVER);
            String subject = HTTPUtils.getParameter(request, SUBJECT);
            String emailMsg = HTTPUtils.getParameter(request, EMAIL_MSG);
            String from = HTTPUtils.getParameter(request, EMAIL_ADDRESS);
            String recipients =
                appProperties.getProperty(AppProperties.CONTACT_US_EMAIL);
            MailUtils.postMail(mailServer, from, recipients, subject, emailMsg,
                _isSendEmail);
        } catch (UserInputException e) {
            return HTTPUtils.warningMsg(request, e.getMessage());
        } catch (Exception e) {
            StringBuffer warningMsg = new StringBuffer();
            warningMsg.append("System Error: Your message was not sent.\n");
            warningMsg
                .append("    (If possible, please contact NCI systems team.)\n");
            warningMsg.append("\n");
            warningMsg.append(e.getMessage() + "\n");
            return HTTPUtils.warningMsg(request, warningMsg);
        }

        HTTPUtils.infoMsg(request, "Your message was successfully sent.");
        clear();
        return "submit";
    }
}
