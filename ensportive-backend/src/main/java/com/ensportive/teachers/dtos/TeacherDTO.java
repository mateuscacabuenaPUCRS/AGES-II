package com.ensportive.teachers.dtos;

import com.ensportive.enums.Sport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TeacherDTO {

    @Schema(name = "id", example = "1", required = true)
    private Long id;
    @Schema(name = "name", example = "Claudia Silva", required = true)
    private String name;
    @Schema(name = "email", example = "claudia.silva@gmail.com", required = true)
    private String email;
    @Schema(name = "cellPhone", example = "5551995401408", required = true)
    private String cellPhone;
    @Schema(name = "sport", example = "TENNIS", required = true)
    private Sport sport;

}