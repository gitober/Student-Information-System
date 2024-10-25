package com.studentinfo.controller;

import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.TeacherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher1, teacher2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");
        teacher1.setEmail("john.doe@email.com");
        teacher1.setPhoneNumber("0402223344");
        teacher1.setRoles(new HashSet<>());  // Initialize roles to prevent null errors

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Williams");
        teacher2.setEmail("jane.williams@email.com");
        teacher2.setPhoneNumber("0403334455");
        teacher2.setRoles(new HashSet<>());  // Initialize roles to prevent null errors
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test to avoid shared state
        reset(teacherService);
    }

    @Test
    void testGetAllTeachers() throws Exception {
        when(teacherService.list()).thenReturn(Arrays.asList(teacher1, teacher2));

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(teacherService, times(1)).list();
    }

    @Test
    void testGetTeacherById() throws Exception {
        when(teacherService.get(1L)).thenReturn(Optional.of(teacher1));

        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(teacherService, times(1)).get(1L);
    }

    @Test
    void testCreateTeacher() throws Exception {
        when(teacherService.save(any(Teacher.class))).thenReturn(teacher1);

        String teacherJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@email.com\",\"phoneNumber\":\"0402223344\",\"roles\":[]}";

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(teacherService, times(1)).save(any(Teacher.class));
    }

    @Test
    void testUpdateTeacher() throws Exception {
        when(teacherService.get(1L)).thenReturn(Optional.of(teacher1));
        when(teacherService.save(any(Teacher.class))).thenReturn(teacher1);

        String teacherJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@email.com\",\"phoneNumber\":\"0402223344\",\"roles\":[]}";

        mockMvc.perform(put("/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(teacherService, times(1)).save(any(Teacher.class));
    }

    @Test
    void testDeleteTeacher() throws Exception {
        when(teacherService.get(1L)).thenReturn(Optional.of(teacher1));
        doNothing().when(teacherService).delete(1L);

        mockMvc.perform(delete("/teachers/1"))
                .andExpect(status().isNoContent());

        verify(teacherService, times(1)).delete(1L);
    }
}
