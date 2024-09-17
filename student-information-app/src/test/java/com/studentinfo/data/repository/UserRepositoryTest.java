package com.studentinfo.data.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.studentinfo.data.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserSaved() {
        // Arrange
        User user = User.builder()
                .username("user1")
                .email("user1@email.com").build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser, "User should be saved");
        assertEquals("user1", savedUser.getUsername(), "Username should be user1");
        assertEquals("user1@Email.com", savedUser.getEmail(), "Email should be user1@email.com");
    }


}