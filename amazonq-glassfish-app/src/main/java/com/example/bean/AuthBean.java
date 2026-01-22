package com.example.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Bean for handling authentication checks
 */
@Named
@RequestScoped
public class AuthBean {

    /**
     * Checks if user is authenticated, redirects to login if not
     */
    public void checkAuthentication() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        
        boolean authenticated = session != null && 
                               session.getAttribute("authenticated") != null && 
                               Boolean.TRUE.equals(session.getAttribute("authenticated"));
        
        System.out.println("AuthBean: Checking authentication, authenticated=" + authenticated);
        
        if (!authenticated) {
            try {
                System.out.println("AuthBean: Redirecting to login page");
                externalContext.redirect(externalContext.getRequestContextPath() + "/");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
