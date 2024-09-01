package com.ensportive.requests.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequestDTO(
        @Schema(name = "description", example = "Solicitação de Cancelamento")
        String description,
        @Schema(name = "userEmail", example = "user@email.com")
        String userEmail,
        @Schema(name = "userName", example = "User Name")
        String userName,
        @Schema(name = "userPhone", example = "999999999")
        String userPhone
) {
}