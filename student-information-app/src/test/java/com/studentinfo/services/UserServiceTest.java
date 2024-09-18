package com.studentinfo.services;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

public class UserServiceTest {

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
    public void testUserSaved() {
        // Arrange
        User user = User.builder()
                .username("user1")
                .email("user1@email.com").build();

        when(userRepository.save(user)).thenReturn(user);

        // Act

        User savedUser = userService.save(user);

        // Assert
        assertNotNull(savedUser, "User should be saved");
        assertEquals("user1", savedUser.getUsername(), "Username should be user1");
        assertEquals("user1@email.com", savedUser.getEmail(), "Email should be user1...");

    }

    @Test
    public void testGetById() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@email.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.get(1L);

        // Assert
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals("user1", foundUser.get().getUsername(), "Username should be user1");
        assertEquals("user1@email.com", foundUser.get().getEmail(), "Email should be user1...");

    }

    @Test
    public void testDelete() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@email.com").build();


        // Act
        userService.delete(1L);

        // Assert
        assertFalse(userRepository.findById(1L).isPresent(), "User should be deleted");

    }

}