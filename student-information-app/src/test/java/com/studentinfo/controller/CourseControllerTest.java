package com.studentinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentinfo.data.dto.CourseCreationRequestDTO;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.CourseTranslation;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security for testing
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    private Course course1, course2;
    private Teacher teacher1, teacher2;

    @BeforeEach
    void setUp() {
        course1 = new Course();
        course1.setCourseId(1L);
        course1.setCourseName("Mathematics");
        course1.setDuration(30);

        course2 = new Course();
        course2.setCourseId(2L);
        course2.setCourseName("Physics");
        course2.setDuration(45);

        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
    }

    @AfterEach
    void tearDown() {
        reset(courseService, teacherService);
    }

    @Test
    public void testGetAllCourses() throws Exception {
        given(courseService.getAllCourses()).willReturn(Arrays.asList(course1, course2));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1L))
                .andExpect(jsonPath("$[0].courseName").value("Mathematics"))
                .andExpect(jsonPath("$[1].courseId").value(2L))
                .andExpect(jsonPath("$[1].courseName").value("Physics"));
    }

    @Test
    public void testGetCourseById() throws Exception {
        given(courseService.getCourseById(1L)).willReturn(Optional.of(course1));

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.courseName").value("Mathematics"));
    }

    @Test
    public void testCreateCourse() throws Exception {
        // Prepare mock data for teachers and translations
        given(teacherService.listByIds(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(teacher1, teacher2));

        // Create a mock list of CourseTranslation for testing
        List<CourseTranslation> mockTranslations = List.of(new CourseTranslation("EN", "course_name", "Mathematics"));
        given(courseService.saveCourse(ArgumentMatchers.any(Course.class), ArgumentMatchers.anyList(), ArgumentMatchers.anyList()))
                .willReturn(course1);

        // Create and set up the CourseCreationRequestDTO
        CourseCreationRequestDTO request = new CourseCreationRequestDTO(course1, Arrays.asList(1L, 2L), mockTranslations);

        String requestJson = objectMapper.writeValueAsString(request);

        ResultActions response = mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.courseName").value("Mathematics"));
    }



    @Test
    public void testUpdateCourse() throws Exception {
        given(courseService.getCourseById(1L)).willReturn(Optional.of(course1));
        given(teacherService.listByIds(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(teacher1, teacher2));

        // Create a mock list of CourseTranslation for testing
        List<CourseTranslation> mockTranslations = List.of(new CourseTranslation("EN", "course_name", "Mathematics"));
        given(courseService.updateCourse(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Course.class), ArgumentMatchers.anyList())).willReturn(course1);

        String courseJson = objectMapper.writeValueAsString(course1);
        String teacherIds = "1,2";

        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("teacherIds", teacherIds)
                        .content(courseJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.courseName").value("Mathematics"));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        given(courseService.deleteCourse(1L)).willReturn(true);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());
    }
}
