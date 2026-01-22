package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles login redirect to Keycloak
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // For demonstration purposes, simulate successful login
        // In production, this would redirect to Keycloak and handle the full OAuth flow
        request.getSession().setAttribute("authenticated", true);
        request.getSession().setAttribute("username", "admin");
        
        response.sendRedirect(request.getContextPath() + "/");
    }
}
