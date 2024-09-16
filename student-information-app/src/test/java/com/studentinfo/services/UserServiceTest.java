package com.studentinfo.services;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Not in use
    }

    @Test
    void authenticateTest() {
        // Test data
        String username = "testuser";
        String password = "testpassword";

        // Mock the UserRepository to return a user with the given username
        //when(userRepository.findByUsername(username)).thenReturn(new User(username, password));

        // Perform authentication
        Optional<User> userOptional = userService.authenticate(username, password);

        // Check if the user is present
        assertTrue(userOptional.isPresent());
    }

}