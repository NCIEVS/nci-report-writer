package gov.nih.nci.evs.reportwriter.bean;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * @author EVS Team
 * @version 1.0
 */

public class LoginFilter implements Filter {
    private static Logger _logger = Logger.getLogger(LoginFilter.class);
    private FilterConfig _filterConfig = null;

    public void destroy() {
        _filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException,
            IllegalStateException {

        try {
            HttpServletRequest requestL = (HttpServletRequest) request;
            HttpServletResponse responseL = (HttpServletResponse) response;

            String contxt = requestL.getContextPath();
            String uri = requestL.getRequestURI();
            int index = uri.lastIndexOf("/");
            String path = uri.substring(index);

            if (path.equals("/login.jsf") || path.equals("/failure.jsf")
                || path.equals("/") || path.equals("/download_nologin.jsf")
                || path.equals("/download.jsf")) {
                chain.doFilter(request, response);
                return;
            }

            HttpSession session = requestL.getSession();
            if (session == null)
                return;

            String queryString = requestL.getQueryString();
            String page = contxt;
            if (path.equals("/logout.jsf")) {
                session.invalidate();
                queryString = "logout";
                responseL.sendRedirect(redirectPage(page, queryString));
                return;
            }

            Boolean svalid = (Boolean) session.getAttribute("isSessionValid");
            if (svalid == null || svalid.equals(Boolean.FALSE)) {
                responseL.sendRedirect(redirectPage(page, queryString));
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
    
    private String redirectPage(String page, String queryString) {
        return page + (queryString == null ? "" : "?" + queryString);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig;
    }
}
