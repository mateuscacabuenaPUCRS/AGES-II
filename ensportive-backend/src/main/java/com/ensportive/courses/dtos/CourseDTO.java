package com.ensportive.courses.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import com.ensportive.enums.Level;
import com.ensportive.enums.PlanType;
import com.ensportive.enums.Sport;
import com.ensportive.teachers.dtos.TeacherDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CourseDTO {

    @Schema(name = "id", example = "1", required = true)
    private Long id;
    @Schema(name = "sport", example = "TENNIS", required = true)
    private Sport sport;
    @Schema(name = "level", example = "ADVANCED", required = true)
    private Level level;
    @Schema(name = "planType", example = "GROUP", required = true)
    private PlanType planType;
    @Schema(name = "studentsSize", example = "4", required = true)
    private Long studentsSize;
    @Schema(name = "weekDay", example = "MONDAY", required = true)
    private DayOfWeek weekDay;
    @Schema(name = "hour", example = "09:00", required = true)
    private LocalTime hour;
    @Schema(name = "court", example = "1", required = true)
    private Long court;
    @Schema(name = "teacher")
    private TeacherDTO teacher;
    @Schema(name = "studentsIds")
    public List<Long> studentsIds;
    @Schema(name = "uniqueDate", example = "2021-09-01", required = false)
    private String uniqueDate;
    @Schema(name = "uniqueLesson", example = "true", required = false)
    private boolean uniqueLesson;

    public void setStudentsIds(List<Long> studentsIds){
        this.studentsIds = studentsIds;
    }
}