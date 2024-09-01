package com.ensportive.lessons.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.ensportive.courses.dtos.CourseDTO;
import com.ensportive.lessonplans.dtos.LessonPlanDTO;
import com.ensportive.studentlessons.dtos.StudentLessonDTO;
import com.ensportive.students.dtos.StudentDTO;
import com.ensportive.teachers.dtos.TeacherDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LessonDTO {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "description", example = "Tennis lesson", required = true)
    private String description;

    @Schema(name = "spot", example = "1", required = true)
    private Integer spot;

    @Schema(name = "date", example = "2022-04-10T10:00:00", required = true)
    private LocalDateTime date;

    @Schema(name = "court", example = "1", required = true)
    private Long court;

    @Schema(name = "course")
    private CourseDTO course;

    @Schema(name = "teacher")
    private TeacherDTO teacher;

    @Schema(name = "secondTeacher")
    private TeacherDTO secondTeacher;

    @Schema(name = "lessonPlan")
    private LessonPlanDTO lessonPlan;

    @Schema(name = "students")
    private List<StudentLessonDTO> courseStudents;

    @Schema(name = "extraStudents")
    private List<StudentLessonDTO> extraStudents;
}
