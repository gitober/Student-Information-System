package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    // Dependencies
    private final AuthenticationManager authenticationManager;

    public SecurityService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Public Methods

    // Authenticate a user using their raw password and set the security context
    public void authenticateUser(User user, String rawPassword) {
        // Convert user roles to Spring Security authorities
        Collection<SimpleGrantedAuthority> authorities = convertRolesToAuthorities(user);

        // Create and authenticate the authentication token
        Authentication authentication = authenticateToken(user.getEmail(), rawPassword, authorities);

        // Set the authenticated context in SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Persist the security context to the session
        saveSecurityContextToSession();
    }

    // Private Helper Methods

    // Convert user roles to Spring Security authorities
    private Collection<SimpleGrantedAuthority> convertRolesToAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    // Create and authenticate the token
    private Authentication authenticateToken(String email, String rawPassword, Collection<SimpleGrantedAuthority> authorities) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, rawPassword, authorities);
        return authenticationManager.authenticate(authToken);
    }

    // Save the security context to the session
    private void saveSecurityContextToSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(), request, response);
    }
}
