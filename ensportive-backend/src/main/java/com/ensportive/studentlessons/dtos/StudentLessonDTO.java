package com.ensportive.studentlessons.dtos;

import com.ensportive.studentlessons.StudentLessonEntity;
import com.ensportive.students.dtos.StudentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentLessonDTO {
    @Schema(name = "id", example = "1")
    Long id;
    @Schema(name = "student")
    StudentDTO student;
    @Schema(name = "attendance", example = "true")
    StudentLessonEntity.Attendance attendance;
    @Schema(name = "extraStudent", example = "false")
    Boolean extraStudent;
}
