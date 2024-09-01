package com.ensportive.licenses.dtos;

import com.ensportive.enums.Frequency;
import com.ensportive.enums.PlanType;
import com.ensportive.enums.Sport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LicenseDTO {
    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "startDate", example = "2021-01-01", required = true)
    private String startDate;

    @Schema(name = "endDate", example = "2021-01-01", required = true)
    private String endDate;

    @Schema(name = "planType", example = "DOUBLES", required = true)
    private PlanType planType;

    @Schema(name = "frequency", example = "ONE", required = true)
    private Frequency frequency;

    @Schema(name = "sport", example = "TENNIS")
    private Sport sport;

    @Schema(name = "coursesPerWeek", example = "3")
    private Long coursesPerWeek;

    @Schema(name = "active", example = "TRUE")
    private Boolean active;

}
