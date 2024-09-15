package com.studentinfo.controller;

import com.studentinfo.data.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeAll
    static void setUp() {
        System.out.println("Starting the userControllerTest class");
    }

    @BeforeEach
    void init() {
        // create an instance of UserController
        userController = new UserController();
    }

    @AfterEach
    void tearDownEach() {
        userController = null;
    }

    // unit test for UserController.getAllUsers()
    @Test
    void testGetAllUsers() {
        // call the getAllUsers() method
        List<User> users = userController.getAllUsers();
        // check if the list of users is not null
        assertNotNull(users);
    }

    // unit test for UserController.getUserById()
    @Test
    void testGetUserById() {
        // call the getUserById() method
        Optional<User> user = userController.getUserById(1L);
        // check if the user is not null
        assertNotNull(user);
    }

    // unit test for UserController.createUser()
    @Test
    void testCreateUser() {
        // create a new user
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        // call the createUser() method
        User newUser = userController.createUser(user);
        // check if the new user is not null
        assertNotNull(newUser);
    }

    // unit test for UserController.updateUser()
    @Test
    void testDeleteUser() {
        // call the deleteUser() method
        userController.deleteUser(1L);
        // check if the user is deleted
        assertNull(userController.getUserById(1L));
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Completed the userControllerTest class");
    }

}