package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    // This method manually sets the authenticated user in the SecurityContextHolder
    public void authenticateUser(User user) {
        // Ensure that roles are prefixed with "ROLE_" to match Spring Security's expectations
        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // Add "ROLE_" prefix
                .collect(Collectors.toList());

        // Create the authentication object with the correct authorities
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Debug: Print out the authorities to verify that they are correctly set
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        // Additional debug statements to ensure correct SecurityContext setup
        System.out.println("Current authenticated user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(authority ->
                System.out.println("Authority: " + authority.getAuthority())
        );
    }
}
