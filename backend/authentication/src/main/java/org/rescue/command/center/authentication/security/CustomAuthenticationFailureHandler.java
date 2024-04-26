package org.rescue.command.center.authentication.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        System.out.println("Authentifizierungsfehler: " + exception.getMessage());
        System.out.println("Authentifizierungsfehler: " + exception.getMessage());
        response.sendRedirect("/login?error=true&message=" + exception.getMessage());
    }
}

