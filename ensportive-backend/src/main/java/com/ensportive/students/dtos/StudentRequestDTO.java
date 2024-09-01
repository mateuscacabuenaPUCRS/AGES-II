package com.ensportive.students.dtos;

import com.ensportive.enums.Level;
import com.ensportive.licenses.dtos.LicenseRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudentRequestDTO(
                @Schema(name = "name", example = "John Doe", required = true) String name,
                @Schema(name = "email", example = "john@doe.com", required = true) String email,
                @Schema(name = "cellPhone", example = "55912349876", required = true) String cellPhone,
                @Schema(name = "level", example = "ADVANCED", required = true) Level level,
                @Schema(name = "license") LicenseRequestDTO license) {
}
