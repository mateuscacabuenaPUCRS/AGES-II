package com.ensportive.teachers.dtos;

import com.ensportive.enums.Sport;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TeacherRequestDTO(

        @NotNull
        @Schema(name = "name", example = "Claudia Silva", required = true) String name,
        @NotNull
        @Schema(name = "email", example = "claudia.silva@gmail.com", required = true) String email,
        @NotNull
        @Schema(name = "cellPhone", example = "5551995401408", required = true) String cellPhone,
        @NotNull
        @Schema(name = "sport", example = "TENNIS", required = true) Sport sport) {
}