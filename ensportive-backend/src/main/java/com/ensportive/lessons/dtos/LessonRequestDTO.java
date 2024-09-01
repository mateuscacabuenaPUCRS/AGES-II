package com.ensportive.lessons.dtos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record LessonRequestDTO(
        @Schema(name = "description", example = "Tennis lesson", required = true) String description,
        @Schema(name = "spot", example = "1", required = true) Integer spot,
        @Schema(name = "date", example = "2022-04-10T10:00:00", required = true) LocalDateTime date,
        @Schema(name = "court", example = "1", required = true) Long court,
        @Schema(name = "teacherId", example = "1", required = true) Long teacherId,
        @Schema(name = "secondTeacherId", example = "2", required = false) Long secondTeacherId,
        @Schema(name = "lessonPlanId", example = "1", required = true) Long lessonPlanId,
        @Schema(name = "courseId", example = "1", required = true) Long courseId) {
}
