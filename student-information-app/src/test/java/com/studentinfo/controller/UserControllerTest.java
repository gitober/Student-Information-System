package com.studentinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentinfo.data.entity.User;
import com.studentinfo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private User user;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {


    }

    @Test
    public void testCreateUser() throws Exception {

        // arrange



        // act

        userController.createUser(user);

        // assert

        assertNotNull(user);

    }

    @Test
    public void testGetAllUsers() {

        userController.getAllUsers();

        assertNotNull(user);

    }

    @Test
    public void testGetUserById() {

        userController.getUserById(1L);

        assertNotNull(user);

    }

    @Test
    public void testDeleteUser() {

        userController.deleteUser(1L);

        assertNull(user);

    }

}
