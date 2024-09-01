package com.ensportive.students.dtos;

import com.ensportive.enums.Level;
import com.ensportive.licenses.dtos.LicenseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentDTO {
    @Schema(name = "id", example = "1", required = true)
    private Long id;
    @Schema(name = "name", example = "John Doe", required = true)
    private String name;
    @Schema(name = "email", example = "john@doe.com", required = true)
    private String email;
    @Schema(name = "cellPhone", example = "55998761234", required = true)
    private String cellPhone;
    @Schema(name = "level", example = "ADVANCED", required = true)
    private Level level;
    @Schema(name = "license")
    private LicenseDTO license;
    @Schema(name ="username")
    private String username;
}
