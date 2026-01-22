package com.example.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

/**
 * Managed bean for handling authentication operations.
 * Uses Jakarta EE OIDC for OAuth integration with Keycloak.
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    /**
     * Initiates the OIDC authentication flow.
     * Redirects user to Keycloak login page.
     */
    public String login() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            // Redirect to Keycloak authorization endpoint with proper redirect URI
            String authUrl = "http://localhost:8180/realms/school-management/protocol/openid_connect/auth" +
                           "?client_id=school-app" +
                           "&response_type=code" +
                           "&scope=openid" +
                           "&redirect_uri=http://localhost:8080/people-app/callback";
            
            externalContext.redirect(authUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Logs out the current user and invalidates the session.
     */
    public String logout() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        
        // Invalidate the session
        request.getSession().invalidate();
        
        // Redirect to main page
        return "/index.xhtml?faces-redirect=true";
    }

    /**
     * Checks if the current user has admin role.
     */
    public boolean isAdmin() {
        HttpServletRequest request = (HttpServletRequest) 
            FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.isUserInRole("admin");
    }
}
