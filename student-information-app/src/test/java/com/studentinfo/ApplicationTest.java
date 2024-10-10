package com.studentinfo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTest {

    @BeforeAll
    static void initAll() {
        // Setup code, if necessary
        System.out.println("Running setup before all tests");
    }

    @Test
    void contextLoads() {
        // If the context fails to load, the test will automatically fail
        System.out.println("Application context loaded successfully");
    }

    @AfterAll
    static void tearDownAll() {
        // Cleanup code, if necessary
        System.out.println("Running cleanup after all tests");
    }
}
