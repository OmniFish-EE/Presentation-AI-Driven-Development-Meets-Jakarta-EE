package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles OAuth callback from Keycloak
 */
@WebServlet("/callback")
public class OAuthCallbackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String code = request.getParameter("code");
        String error = request.getParameter("error");
        
        if (error != null) {
            response.sendRedirect(request.getContextPath() + "/?error=" + error);
            return;
        }
        
        if (code != null) {
            // For now, just redirect to main page - in full implementation 
            // we would exchange code for token here
            request.getSession().setAttribute("authenticated", true);
            response.sendRedirect(request.getContextPath() + "/people.xhtml");
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
