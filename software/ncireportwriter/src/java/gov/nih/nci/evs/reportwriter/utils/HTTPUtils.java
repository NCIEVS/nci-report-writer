package gov.nih.nci.evs.reportwriter.utils;

import java.util.*;
import java.util.regex.*;

import javax.faces.context.*;
import javax.servlet.http.*;

/**
 * HTTP Utility methods
 * @author garciawa2
 *
 */
public class HTTPUtils {

    /**
     * Remove potentially bad XSS syntax
     * @param value
     * @return
     */

    public static String cleanXSS(String value) {

        if (value == null || value.length() < 1)
            return value;

        // Remove XSS attacks
        value = replaceAll(value,"<\\s*script\\s*>.*</\\s*script\\s*>", "");
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = replaceAll(value,"[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("\"", "&quot;");
        return value;

    }

    /**
     * @param string
     * @param regex
     * @param replaceWith
     * @return
     */
    public static String replaceAll(String string, String regex, String replaceWith) {

        Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        string = myPattern.matcher(string).replaceAll(replaceWith);
        return string;

    }
    
    public static void printRequestSessionAttributes() {
        System.out.println(" ");
        System.out.println(StringUtils.SEPARATOR);
        System.out.println("Request Session Attribute(s):");

        try {
            HttpServletRequest request = (HttpServletRequest)FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
    
            HttpSession session = request.getSession();
            Enumeration<?> enumeration = SortUtils.sort(session.getAttributeNames());
            int i=0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = session.getAttribute(name);
                System.out.println("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " +
                e.getMessage());
        }
    }

    public static void printRequestAttributes() {
        System.out.println(" ");
        System.out.println(StringUtils.SEPARATOR);
        System.out.println("Request Attribute(s):");

        try {
            HttpServletRequest request = (HttpServletRequest)FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
    
            Enumeration<?> enumeration = SortUtils.sort(request.getAttributeNames());
            int i=0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getAttribute(name);
                System.out.println("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " +
                e.getMessage());
        }
    }
    
    public static void printRequestParameters() {
        System.out.println(" ");
        System.out.println(StringUtils.SEPARATOR);
        System.out.println("Request Parameter(s):");

        try {
            HttpServletRequest request = (HttpServletRequest)FacesContext.
                getCurrentInstance().getExternalContext().getRequest();

            Enumeration<?> enumeration = SortUtils.sort(request.getParameterNames());
            int i=0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getParameter(name);
                System.out.println("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " +
                e.getMessage());
        }
    }
    
    public static void printAttributes() {
        printRequestSessionAttributes();
        printRequestAttributes();
        printRequestParameters();
        System.out.println(" ");
    }

    public static String warningMsg(HttpServletRequest request, String msg) {
        request.setAttribute("warningMsg", msg);
        return "warningMsg";
    }
    
    public static String warningMsg(HttpServletRequest request, StringBuffer buffer) {
        return warningMsg(request, buffer.toString());
    }

    public static String warningMsg(HttpServletRequest request, StringBuffer buffer, 
        Throwable throwable) {
        if (buffer.length() > 0 && buffer.charAt(buffer.length()-1) != '\n')
            buffer.append("\n");
        buffer.append(throwable.getClass().getSimpleName() + ": " + 
            throwable.getMessage());
        return warningMsg(request, buffer);
    }

    public static String getParameter(HttpServletRequest request, String parameterName) {
        String value = request.getParameter(parameterName);
        value = value.trim();
        request.setAttribute(parameterName, value);
        return value;
    }
    
    public static String getSessionAttributeStr(HttpServletRequest request,
        String attributeName) {
        String value =
            (String) request.getSession().getAttribute(attributeName);
        value = value.trim();
        request.setAttribute(attributeName, value);
        return value;
    }

}
