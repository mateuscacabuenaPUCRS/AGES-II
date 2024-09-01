package com.ensportive.lessonplans.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.ensportive.enums.Level;

@Data
public class LessonPlanDTO {
    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "modality", example = "três sets de saques", required = true)
    private String modality;

    @Schema(name = "warmUp", example = "três sets de ataque", required = true)
    private String warmUp;

    @Schema(name = "technique1", example = "uma série de backhand", required = true)
    private String technique1;

    @Schema(name = "technique2", example = "5 minutos de corrida", required = true)
    private String technique2;

    @Schema(name = "tactic", example = "três sets de slice", required = true)
    private String tactic;

    @Schema(name = "serve", example = "saque alto", required = true)
    private String serve;

    @Schema(name = "social", example = "saque alto", required = true)
    private String social;

    @Schema(name = "level", example = "ADVANCED", required = true)
    private Level level;
}