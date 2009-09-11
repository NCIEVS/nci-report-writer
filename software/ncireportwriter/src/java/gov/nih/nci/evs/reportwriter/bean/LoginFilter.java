package gov.nih.nci.evs.reportwriter.bean;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * @author EVS Team
 * @version 1.0
 * 
 *          Modification history Initial implementation rajasimhah@mail.nih.gov
 * 
 */

public class LoginFilter implements Filter {

    private FilterConfig filterConfig = null;

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException,
            IllegalStateException {

        try {
            HttpServletRequest hsr = (HttpServletRequest) request;
            HttpServletResponse hsr2 = (HttpServletResponse) response;

            String contxt = hsr.getContextPath();
            String uri = hsr.getRequestURI();
            int index = uri.lastIndexOf("/");
            String path = uri.substring(index);
            String queryString = hsr.getQueryString();
            String page = contxt;

            if (path.equals("/login.jsf") || path.equals("/failure.jsf")
                || path.equals("/") || path.equals("/download_nologin.jsf")
                || path.equals("/download.jsf")) {
                chain.doFilter(request, response);
            } else {
                try {
                    HttpSession session = hsr.getSession();

                    Boolean svalid = null;
                    if (session != null) {
                        if (path.equals("/logout.jsf")) {
                            try {
                                session.invalidate();
                                queryString = "logout";
                                hsr2.sendRedirect(page
                                    + (queryString == null ? "" : "?"
                                        + queryString));
                            } catch (Exception e) {
                                // e.printStackTrace();
                            }
                        }
                        try {
                            svalid =
                                (Boolean) session
                                    .getAttribute("isSessionValid");
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                        if (svalid == null || svalid.equals(Boolean.FALSE)) {
                            hsr2
                                .sendRedirect(page
                                    + (queryString == null ? "" : "?"
                                        + queryString));
                        } else {
                            try {
                                chain.doFilter(request, response);
                            } catch (Exception e) {
                                // e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

}
