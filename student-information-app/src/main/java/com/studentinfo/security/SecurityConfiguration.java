package com.studentinfo.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurity {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    logger.info("Session management configured to IF_REQUIRED.");
                    logCurrentSecurityContext("After session management configuration");
                })
                .securityContext(securityContext -> {
                    securityContext.securityContextRepository(securityContextRepository());
                    logger.info("Security context repository set.");
                    logCurrentSecurityContext("After setting security context repository");
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/register", "/images/**", "/forgotpassword").permitAll();
                    auth.requestMatchers("/profile", "/courses", "/editprofile", "/grades").hasRole("USER");
                    logger.info("Configured request matchers for authorization.");
                    logCurrentSecurityContext("After configuring authorization requests");
                })
                .formLogin(form -> {
                    form.loginPage("/login").defaultSuccessUrl("/profile", true);
                    logger.info("Form login configured.");
                    logCurrentSecurityContext("After configuring form login");
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout")
                            .invalidateHttpSession(true)  // Ensure session invalidation
                            .clearAuthentication(true)   // Clear authentication
                            .deleteCookies("JSESSIONID", "XSRF-TOKEN") // Delete cookies to avoid residual session data
                            .logoutSuccessUrl("/login") // Redirect to login page after logout
                            .addLogoutHandler((request, response, authentication) -> {
                                // Additional cleanup if needed
                                logger.info("Session invalidated during logout.");
                            })
                            .logoutSuccessHandler((request, response, authentication) -> {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.sendRedirect("/login");
                                logger.info("Successfully logged out and redirected to login.");
                            });
                    logCurrentSecurityContext("After configuring logout");
                })
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint((request, response, authException) -> {
                        logger.warn("Unauthenticated access attempt. Redirecting to /login.");
                        response.sendRedirect("/login");
                    });
                    logCurrentSecurityContext("After configuring exception handling");
                });

        // Ensure correct filter placement: adding AnonymousAuthenticationFilter should not overwrite authenticated contexts
        http.addFilterAfter(new AnonymousAuthenticationFilter("key"), SecurityContextPersistenceFilter.class);
        logCurrentSecurityContext("After adding AnonymousAuthenticationFilter");

        super.configure(http);
        logCurrentSecurityContext("After super.configure(http)");
    }

    // Utility method to log the current security context
    private void logCurrentSecurityContext(String phase) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Current Authentication during {}: User - {}, Authorities - {}", phase, auth.getName(), auth.getAuthorities());
        } else {
            logger.warn("No authentication found in SecurityContextHolder during {}.", phase);
        }
    }
}
