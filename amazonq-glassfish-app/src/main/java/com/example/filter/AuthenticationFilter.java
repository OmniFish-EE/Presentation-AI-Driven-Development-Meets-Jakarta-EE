package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter to protect pages that require authentication
 */
@WebFilter(urlPatterns = {"/people.xhtml", "/groups.xhtml", "/schedule.xhtml", "/api/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        System.out.println("AuthenticationFilter: Checking " + httpRequest.getRequestURI());
        
        boolean authenticated = session != null && 
                               session.getAttribute("authenticated") != null && 
                               Boolean.TRUE.equals(session.getAttribute("authenticated"));
        
        System.out.println("AuthenticationFilter: authenticated=" + authenticated);
        
        if (!authenticated) {
            System.out.println("AuthenticationFilter: Redirecting to login");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
            return;
        }
        
        chain.doFilter(request, response);
    }
}
