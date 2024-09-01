package com.ensportive.lessonplans.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import com.ensportive.enums.Level;

public record LessonPlanRequestDTO(

        @NotNull
        @Schema(name = "id", example = "1", required = true) Long id,

        @NotNull
        @Schema(name = "modality", example = "Beach Tennis - Avançado", required = true) String modality,

        @NotNull
        @Schema(name = "warmUp", example = "5 minutos correndo na quadra", required = true) String warmUp,

        @NotNull
        @Schema(name = "technique1", example = "Uma série de backhand", required = true) String technique1,

        @NotNull
        @Schema(name = "technique2", example = "Três sets de forehand", required = true) String technique2,

        @NotNull
        @Schema(name = "tactic", example = "Saída de rede", required = true) String tactic,

        @NotNull
        @Schema(name = "serve", example = "Lob", required = true) String serve,

        @NotNull
        @Schema(name = "social", example = "dois games entre si", required = true) String social,
        
        @NotNull
        @Schema(name = "level", example = "ADVANCED", required = true) Level level)
{
}