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

    private final AuthenticationManager authenticationManager;

    public SecurityService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void authenticateUser(User user, String rawPassword) {
        // Convert user roles to Spring Security authorities
        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        // Create the authentication token with user email and raw password (not hashed password)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(), rawPassword, authorities);

        // Authenticate the token using the AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Set the authenticated context in SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Let Spring Security handle the session persistence
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        // Save the security context to the session (this is normally handled automatically)
        new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(), request, response);
    }
}
