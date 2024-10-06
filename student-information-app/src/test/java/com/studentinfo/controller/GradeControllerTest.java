package com.studentinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.GradeService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Grade grade1, grade2;
    private Course course1, course2;

    @BeforeEach
    void setUp() {
        // Set up courses
        course1 = new Course();
        course1.setCourseId(200L);
        course1.setCourseName("Mathematics");
        course1.setDuration(90); // Set duration to prevent NullPointerException

        course2 = new Course();
        course2.setCourseId(201L);
        course2.setCourseName("Physics");
        course2.setDuration(120); // Set duration to prevent NullPointerException

        // Set up grades
        grade1 = new Grade();
        grade1.setGradeId(1);
        grade1.setGrade("A");
        grade1.setGradingDay(LocalDate.of(2024, 9, 30));
        grade1.setStudentNumber(100L);
        grade1.setCourse(course1);

        grade2 = new Grade();
        grade2.setGradeId(2);
        grade2.setGrade("B");
        grade2.setGradingDay(LocalDate.of(2024, 10, 1));
        grade2.setStudentNumber(101L);
        grade2.setCourse(course2);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test to ensure no shared state
        reset(gradeService);
    }

    @Test
    public void testGetGradesByStudentNumber() throws Exception {
        List<Grade> grades = Arrays.asList(grade1);
        given(gradeService.getGradesByStudentNumber(100L)).willReturn(grades);

        mockMvc.perform(get("/api/grades/student/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gradeId").value(1))
                .andExpect(jsonPath("$[0].grade").value("A"))
                .andExpect(jsonPath("$[0].gradingDay").value("2024-09-30"))
                .andExpect(jsonPath("$[0].studentNumber").value(100L))
                .andExpect(jsonPath("$[0].course.courseId").value(200L));
    }

    @Test
    public void testGetGradesByCourseId() throws Exception {
        List<Grade> grades = Arrays.asList(grade2);
        given(gradeService.getGradesByCourseId(201L)).willReturn(grades);

        mockMvc.perform(get("/api/grades/course/201"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gradeId").value(2))
                .andExpect(jsonPath("$[0].grade").value("B"))
                .andExpect(jsonPath("$[0].gradingDay").value("2024-10-01"))
                .andExpect(jsonPath("$[0].course.courseId").value(201L));
    }

    @Test
    public void testCreateGrade() throws Exception {
        given(gradeService.saveGrade(ArgumentMatchers.any(Grade.class))).willReturn(grade1);

        String gradeJson = objectMapper.writeValueAsString(grade1);

        ResultActions response = mockMvc.perform(post("/api/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gradeJson));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.gradeId").value(1))
                .andExpect(jsonPath("$.grade").value("A"))
                .andExpect(jsonPath("$.gradingDay").value("2024-09-30"))
                .andExpect(jsonPath("$.studentNumber").value(100L))
                .andExpect(jsonPath("$.course.courseId").value(200L));
    }

    @Test
    public void testUpdateGrade() throws Exception {
        // Mock the updateGrade method to simulate finding and updating the grade
        given(gradeService.updateGrade(ArgumentMatchers.eq(1), ArgumentMatchers.any(Grade.class))).willReturn(grade1);

        // Convert the grade object to JSON
        String gradeJson = objectMapper.writeValueAsString(grade1);

        // Perform the PUT request to update the grade
        mockMvc.perform(put("/api/grades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gradeJson))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.gradeId").value(1))
                .andExpect(jsonPath("$.grade").value("A"))
                .andExpect(jsonPath("$.gradingDay").value("2024-09-30"))
                .andExpect(jsonPath("$.studentNumber").value(100L))
                .andExpect(jsonPath("$.course.courseId").value(200L));
    }

    @Test
    public void testDeleteGrade() throws Exception {
        when(gradeService.deleteGrade(1)).thenReturn(true);

        mockMvc.perform(delete("/api/grades/1"))
                .andExpect(status().isNoContent());
    }
}
