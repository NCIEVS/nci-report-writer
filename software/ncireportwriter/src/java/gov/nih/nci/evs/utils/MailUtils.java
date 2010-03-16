package gov.nih.nci.evs.utils;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

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

public class MailUtils extends Object {
    private static final long serialVersionUID = 1L;
    private static final int MAX_SUBJECT_CHAR = 256;
    private static final String INDENT = "    ";

    public static boolean isValidEmailAddress(String text) {
        int posOfAtChar = text.indexOf('@');
        int posOfDotChar = text.lastIndexOf('.');

        if (posOfAtChar <= 0 || posOfDotChar <= 0)
            return false;
        if (posOfAtChar > posOfDotChar)
            return false;
        if (posOfAtChar == posOfDotChar - 1)
            return false;
        if (posOfDotChar == text.length() - 1)
            return false;
        return true;
    }

    private static void postMailValidation(String[] senderList,
        String[] recipientList, String subject, String message)
            throws UserInputException {
        validateMissingFields(senderList, recipientList, subject, message);
        validEmailAddresses(senderList, "sender");
        validEmailAddresses(recipientList, "recipient");
    }

    private static void validateMissingFields(String[] senderList,
        String[] recipientList, String subject, String message)
            throws UserInputException {
        StringBuffer buffer = new StringBuffer();
        int ctr = 0;

        if (subject == null || subject.length() <= 0) {
            buffer.append(INDENT + "* subject of your email\n");
            ++ctr;
        }
        if (message == null || message.length() <= 0) {
            buffer.append(INDENT + "* detailed description\n");
            ++ctr;
        }

        if (recipientList == null || recipientList.length <= 0
            || recipientList[0].trim().length() <= 0) {
            buffer.append(INDENT + "* recipient e-mail address\n");
            ++ctr;
        }

        if (senderList == null || senderList.length <= 0
            || senderList[0].trim().length() <= 0) {
            buffer.append(INDENT + "* sender e-mail address\n");
            ++ctr;
        }

        if (buffer.length() <= 0)
            return;

        String s = "Warning: Your message was not sent.\n";
        s +=
            "The following field" + WordUtils.was_were(ctr, "s")
                + " not set:\n";
        buffer.insert(0, s);
        throw new UserInputException(buffer.toString());
    }

    public static void validEmailAddresses(String[] emailList, String emailType)
            throws UserInputException {
        StringBuffer buffer = new StringBuffer();
        int ctr = 0;

        for (int i = 0; i < emailList.length; ++i) {
            String email = emailList[i];
            if (isValidEmailAddress(email))
                continue;
            buffer.append(INDENT + INDENT + "* " + email + "\n");
            ++ctr;
        }
        if (buffer.length() <= 0)
            return;

        String s = "Warning: Your message was not sent.\n";
        s +=
            INDENT + "* Invalid " + WordUtils.addEndingSpaceIfNeeded(emailType)
                + "e-mail address" + WordUtils.plural(ctr, "es") + ":\n";
        buffer.insert(0, s);
        throw new UserInputException(buffer.toString());
    }

    public static void postMail(String mailSmtpServer, String senders,
        String recipients, String subject, String message, boolean send)
            throws MessagingException, Exception {
        if (mailSmtpServer == null || mailSmtpServer.length() <= 0)
            throw new MessagingException("SMTP host not set.");

        String senderList[] = StringUtils.toStrings(senders, ";", false);
        String recipientList[] = StringUtils.toStrings(recipients, ";", false);
        subject = StringUtils.truncate(MAX_SUBJECT_CHAR, subject);
        postMailValidation(senderList, recipientList, subject, message);

        for (int i = 0; i < senderList.length; ++i) {
            String sender = senderList[i];

            // Sets the host smtp address.
            Properties props = new Properties();
            props.put("mail.smtp.host", mailSmtpServer);

            // Creates some properties and get the default session.
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(false);

            // Creates a message.
            Message msg = new MimeMessage(session);

            // Sets the from and recipient addresses.
            InternetAddress senderAddress = new InternetAddress(sender);
            msg.setFrom(senderAddress);
            msg.setRecipient(Message.RecipientType.BCC, senderAddress);

            InternetAddress[] recipientAddress =
                new InternetAddress[recipientList.length];
            for (int j = 0; j < recipientList.length; j++)
                recipientAddress[j] = new InternetAddress(recipientList[j]);
            msg.setRecipients(Message.RecipientType.TO, recipientAddress);

            // Optional: You can set your custom headers if you want.
            msg.addHeader("MyHeaderName", "myHeaderValue");

            // Setting the Subject and Content Type.
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            if (send)
                Transport.send(msg);
        }
    }

    public static void postMail(String mailSmtpServer, String senders,
        String recipients, String subject, String message)
            throws MessagingException, Exception {
        postMail(mailSmtpServer, senders, recipients, subject, message, true);
    }
}
