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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurity {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final UserDetailsServiceImpl userDetailsService;
    private final DataSource dataSource; // Inject the DataSource

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
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

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
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
                    auth.requestMatchers("/profile", "/courses", "/editprofile", "/grades", "/teacher/attendance-tracking", "teacher/update-students").hasRole("USER");
                    logger.info("Configured request matchers for authorization.");
                    logCurrentSecurityContext("After configuring authorization requests");
                })
                .formLogin(form -> {
                    form.loginPage("/login").defaultSuccessUrl("/profile", true);
                    logger.info("Form login configured.");
                    logCurrentSecurityContext("After configuring form login");
                })
                .rememberMe(rememberMe -> {
                    rememberMe.tokenRepository(persistentTokenRepository())
                            .tokenValiditySeconds(1209600) // 2 weeks
                            .rememberMeParameter("remember-me"); // This corresponds to the checkbox in your login form
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                            .logoutSuccessUrl("/login")
                            .addLogoutHandler((request, response, authentication) -> {
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
                })
                .anonymous(anonymous -> anonymous.authorities("ROLE_ANONYMOUS"));

        logCurrentSecurityContext("After anonymous configuration");

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
