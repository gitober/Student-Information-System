package com.studentinfo.controller;

import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.TeacherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        teacher2 = new Teacher();

        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");
        teacher1.setEmail("john.doe@email.com");
        teacher1.setPhoneNumber("0402223344");

        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Williams");
        teacher2.setEmail("jane.williams@email.com");
        teacher2.setPhoneNumber("0403334455");
    }

    @AfterEach
    void tearDown() {
        teacher1 = null;
        teacher2 = null;
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

        String teacherJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"subject\":\"Math\"}";

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(teacherService, times(1)).save(any(Teacher.class));
    }

    @Test
    void testUpdateTeacher() throws Exception {

        when(teacherService.save(any(Teacher.class))).thenReturn(teacher1);

        String teacherJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"subject\":\"Math\"}";

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
        doNothing().when(teacherService).delete(1L);

        mockMvc.perform(delete("/teachers/1"))
                .andExpect(status().isOk());

        verify(teacherService, times(1)).delete(1L);
    }
}
