package com.studentinfo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.reset;

@WebMvcTest(LogoutController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize mock data, set default mock behaviors, or configure shared objects
        SecurityContextHolder.clearContext(); // Clear security context before each test
    }

    @AfterEach
    void tearDown() {
        // Reset mock beans if needed, e.g., reset(translationService);
        SecurityContextHolder.clearContext(); // Clear the security context to prevent state leakage between tests
    }



    @Test
    @WithMockUser // Simulate an authenticated user
    public void testLogout() throws Exception {
        // Perform the logout request
        ResultActions response = mockMvc.perform(get("/logout"));

        // Verify the response status and headers
        response.andExpect(status().isFound()) // Check for HTTP 302 redirect
                .andExpect(header().string("Location", "/login")); // Check the redirection header
    }
}
