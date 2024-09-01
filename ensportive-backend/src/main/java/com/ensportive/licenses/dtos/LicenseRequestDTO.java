package com.ensportive.licenses.dtos;

import com.ensportive.enums.Frequency;
import com.ensportive.enums.PlanType;
import com.ensportive.enums.Sport;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record LicenseRequestDTO(

        @Schema(name = "startDate", example = "2021-01-01") LocalDate startDate,

        @Schema(name = "endDate", example = "2021-01-01") LocalDate endDate,

        @Schema(name = "planType", example = "DOUBLES") PlanType planType,

        @Schema(name = "frequency", example = "ONE") Frequency frequency,

        @Schema(name = "sport", example = "TENNIS") Sport sport,

        @Schema(name = "coursesPerWeek", example = "3") Long coursesPerWeek,

        @Schema(name = "active", example = "TRUE") Boolean active) {
}