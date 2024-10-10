package com.studentinfo.security;

import com.studentinfo.data.entity.Role;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Ensure the user exists in the database before each test
        User user = new User();
        user.setEmail("validuser@example.com");
        user.setUsername("validuser");
        user.setHashedPassword(passwordEncoder.encode("validpassword"));
        user.setUserType("USER");
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database or reset configurations after each test
        userRepository.deleteAll();
    }

    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(formLogin("/login")
                        .user("email", "validuser@example.com")
                        .password("validpassword"))
                .andExpect(status().isFound())
                .andExpect(authenticated().withUsername("validuser@example.com"));
    }

    @Test
    void testLoginFailure() throws Exception {
        mockMvc.perform(formLogin("/login")
                        .user("email", "invaliduser@example.com")
                        .password("invalidpassword"))
                .andExpect(status().isFound())
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "validuser@example.com", roles = {"USER"})
    void testAccessToProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("validuser@example.com"));
    }

    @Test
    void testAccessToProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isFound())
                .andExpect(unauthenticated());
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().isFound())
                .andExpect(unauthenticated());
    }
}
