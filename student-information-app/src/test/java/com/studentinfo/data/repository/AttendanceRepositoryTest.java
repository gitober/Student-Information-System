package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setBirthday(LocalDate.of(2000, 1, 1));
        student.setEmail("test@example.com");
        student.setFirstName("John");
        student.setHashedPassword("hashed_password");
        student.setLastName("Doe");
        student.setPhoneNumber("1234567890");
        student.setStudentNumber(12345L);
        student.setUserType("STUDENT");
        student.setUsername("johndoe");
        studentRepository.saveAndFlush(student);

        course = new Course();
        course.setCourseName("Sample Course");
        course.setCoursePlan("Sample Plan");
        course.setDuration(30);
        courseRepository.saveAndFlush(course);

        Attendance attendance = new Attendance();
        attendance.setCourse(course);
        attendance.setStudent(student);
        attendance.setAttendanceStatus("Present");
        attendance.setAttendanceDate(LocalDate.now());
        attendanceRepository.saveAndFlush(attendance);
    }

    @Test
    void testFindByStudent_StudentNumberAndCourse_CourseId() {
        List<Attendance> attendanceList = attendanceRepository.findByStudent_StudentNumberAndCourse_CourseId(
                student.getStudentNumber(), course.getCourseId());
        assertNotNull(attendanceList);
        assertEquals(1, attendanceList.size());
    }


    @Test
    void testFindByCourse_CourseId() {
        List<Attendance> attendanceList = attendanceRepository.findByCourse_CourseId(course.getCourseId());
        assertNotNull(attendanceList);
        assertEquals(1, attendanceList.size());
    }

    @Test
    void testFindByCourseIn() {
        List<Attendance> attendanceList = attendanceRepository.findByCourseIn(List.of(course));
        assertNotNull(attendanceList);
        assertEquals(1, attendanceList.size());
    }
}
