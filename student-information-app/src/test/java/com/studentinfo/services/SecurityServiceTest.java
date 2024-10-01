package com.studentinfo.services;

import com.studentinfo.data.entity.Role;
import com.studentinfo.data.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RequestContextHolder requestContextHolder;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Disabled
    void testAuthenticateUserSuccess() {
        // Arrange
        User user = new User();

        String email = "test@example.com";
        String rawPassword = "password";

        user.setEmail(email);
        user.setRoles(Collections.singleton(Role.USER));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Act
        securityService.authenticateUser(user, rawPassword);

        // Assert
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(new HttpSessionSecurityContextRepository(), times(1)).saveContext(SecurityContextHolder.getContext(), request, response);
    }

    @Test
    void testAuthenticateUserFailure() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setRoles(Collections.singleton(Role.USER));

        String rawPassword = "wrongpassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> securityService.authenticateUser(user, rawPassword));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

//    @Test
//    void testConvertRolesToAuthorities() {
//        // Arrange
//        User user = new User();
//        user.setRoles(Collections.singleton(Role.USER));
//
//        // Act
//        var authorities = securityService.convertRolesToAuthorities(user);
//
//        // Assert
//        assertEquals(1, authorities.size());
//        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
//    }
//
//    @Test
//    void testConvertEmptyRolesToAuthorities() {
//        // Arrange
//        User user = new User();
//        user.setRoles(Collections.emptySet());
//
//        // Act
//        var authorities = securityService.convertRolesToAuthorities(user);
//
//        // Assert
//        assertTrue(authorities.isEmpty());
//    }

    // You can add more tests for edge cases or additional methods in SecurityService here.
}