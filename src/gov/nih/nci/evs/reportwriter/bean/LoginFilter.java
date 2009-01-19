package gov.nih.nci.evs.reportwriter.bean;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	private FilterConfig filterConfig = null;

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse
			  response, FilterChain chain) throws IOException, ServletException {

		try {
			HttpServletRequest hsr = (HttpServletRequest) request;
			HttpServletResponse hsr2 = (HttpServletResponse) response;
			
		    String contxt = hsr.getContextPath();
		    String uri = hsr.getRequestURI();
		    int index = uri.lastIndexOf("/");
		    String path = uri.substring(index);
		
		    if (path.equals("/login.jsf") || path.equals("/") || path.equals("/download_nologin.jsf")) {
		        chain.doFilter(request, response);
		    }
		    else {
		    	HttpSession session = hsr.getSession();
		    	
		    	Boolean svalid = null;
		    	if(session != null) 
		    		svalid = (Boolean) session.getAttribute("isSessionValid");
		    	if(svalid != null)
		    		System.out.println("is session valid? " + svalid.toString());
		    			    	
		        if(svalid == null || svalid.equals(Boolean.FALSE)) {
		    		  String queryString = hsr.getQueryString();
		              String page=contxt;
		              hsr2.sendRedirect(page + (queryString == null ? "" :
		  "?" + queryString));
		         }
		         else {
		       	  	 chain.doFilter(request, response);
		         }
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}
