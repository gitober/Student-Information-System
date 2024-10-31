package com.studentinfo.data.dto;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.CourseTranslation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreationRequestDTO {
    private Course course;                      // Course details
    private List<Long> teacherIds;              // List of teacher IDs associated with the course
    private List<CourseTranslation> translations; // List of course translations
}
