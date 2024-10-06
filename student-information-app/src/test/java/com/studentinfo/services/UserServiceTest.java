package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class UserServiceTest {

    private AutoCloseable mocks;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close mocks to avoid any side effects
        mocks.close();

        // Reset mocks to ensure no shared state between tests
        reset(userRepository, passwordEncoder, authenticationManager);
    }

    @Test
    void testAuthenticateSuccess() {
        String userEmail = "test@email.com";
        String password = "testPassword";
        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getHashedPassword())).thenReturn(true);

        Optional<User> result = userService.authenticate(userEmail, password);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testAuthenticateFailure() {
        String userEmail = "test@email.com";
        String password = "wrongPassword";
        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getHashedPassword())).thenReturn(false);

        Optional<User> result = userService.authenticate(userEmail, password);

        assertFalse(result.isPresent());
    }

    @Test
    void testListUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.list();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.get(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testSaveUser() {
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;

        doNothing().when(userRepository).deleteById(id);

        userService.delete(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}
