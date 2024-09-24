package com.studentinfo.data.repository;

import com.studentinfo.data.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test") // application-test.properties used
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a test user
        testUser = new User();
        testUser.setUsername("john_doe");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");

        // Add this line to set the user_type
        testUser.setUserType("USER");

        // Save the user to the database
        userRepository.save(testUser);
    }


    @Test
    void findByUsername() {
        User user = userRepository.findByUsername("john_doe");

        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void findByEmail() {
        User user = userRepository.findByEmail("john.doe@example.com");

        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
    }
}
