package com.ensportive.requests.dtos;


import io.swagger.v3.oas.annotations.media.Schema;

public record AbsenceRequestDTO(
        @Schema(name = "description", example = "Solicitação de Cancelamento")
        String description,
        @Schema(name = "lessonId", example = "1")
        Long lessonId)
{

}