package com.studentinfo.services;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(teacherRepository, attendanceRepository, studentRepository, courseRepository);
    }

    @Test
    void testGetTeacherById() {
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.get(teacherId);

        assertTrue(result.isPresent());
        assertEquals(teacherId, result.get().getId());
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void testSaveTeacher() {
        Teacher teacher = new Teacher();

        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher result = teacherService.save(teacher);

        assertEquals(teacher, result);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testDeleteTeacher() {
        Long teacherId = 1L;

        doNothing().when(teacherRepository).deleteById(teacherId);

        teacherService.delete(teacherId);

        verify(teacherRepository, times(1)).deleteById(teacherId);
    }

    @Test
    void testListTeachers() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        List<Teacher> teachers = List.of(teacher1, teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.list();

        assertEquals(2, result.size());
        assertTrue(result.contains(teacher1));
        assertTrue(result.contains(teacher2));
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testGetTeacherByUsername() {
        String username = "teacherUser";
        Teacher teacher = new Teacher();
        teacher.setUsername(username);

        when(teacherRepository.findByUsername(username)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.getTeacherByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(teacherRepository, times(1)).findByUsername(username);
    }

    @Test
    void testSaveAttendanceRecord() {
        Attendance attendance = new Attendance();

        when(attendanceRepository.save(attendance)).thenReturn(attendance);

        Attendance result = teacherService.saveAttendanceRecord(attendance);

        assertEquals(attendance, result);
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    void testGetAttendanceRecordsForTeacher() {
        Long teacherId = 1L;
        Course course = new Course();
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setCourses(List.of(course));
        List<Attendance> attendanceList = List.of(new Attendance());

        when(teacherRepository.findTeacherWithCourses(teacherId)).thenReturn(teacher);
        when(attendanceRepository.findByCourseIn(List.of(course))).thenReturn(attendanceList);

        List<Attendance> result = teacherService.getAttendanceRecordsForTeacher(teacherId);

        assertEquals(1, result.size());
        verify(teacherRepository, times(1)).findTeacherWithCourses(teacherId);
        verify(attendanceRepository, times(1)).findByCourseIn(List.of(course));
    }

    @Test
    void testGetCoursesForTeacher() {
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        Course course = new Course();
        teacher.setCourses(List.of(course));

        when(teacherRepository.findTeacherWithCourses(teacherId)).thenReturn(teacher);

        List<Course> result = teacherService.getCoursesForTeacher(teacherId);

        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
        verify(teacherRepository, times(1)).findTeacherWithCourses(teacherId);
    }
}
