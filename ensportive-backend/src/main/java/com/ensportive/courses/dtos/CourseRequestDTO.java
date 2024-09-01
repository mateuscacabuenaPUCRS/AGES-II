package com.ensportive.courses.dtos;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import com.ensportive.enums.Level;
import com.ensportive.enums.PlanType;
import com.ensportive.enums.Sport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Enumerated;

public record CourseRequestDTO(
        @Schema(name = "sport", example = "TENNIS") Sport sport,
        @Schema(name = "level", example = "ADVANCED") Level level,
        @Schema(name = "planType", example = "GROUP") PlanType planType,
        @Schema(name = "studentsSize", example = "4") Integer studentsSize,
        @Schema(name = "hour", example = "09:00") LocalTime hour,
        @Schema(name = "weekDay", example = "MONDAY") DayOfWeek weekDay,
        @Schema(name = "uniqueDate", example = "2021-09-01") LocalDate uniqueDate,
        @Schema(name = "uniqueLesson", example = "true") boolean uniqueLesson,
        @Schema(name = "teacherId", example = "1") Long teacherId,
        @Schema(name = "court", example = "1") Long court,
        @Schema(name = "studentsIds", example = "[1, 2, 3, 4]") Long[] studentsIds)
{}