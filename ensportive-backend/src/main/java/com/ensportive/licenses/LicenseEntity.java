package com.ensportive.licenses;

import com.ensportive.enums.Frequency;
import com.ensportive.enums.PlanType;
import com.ensportive.enums.Sport;
import com.ensportive.students.StudentEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "licenses")
public class LicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "license_id")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type")
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private Frequency frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport")
    private Sport sport;

    @Column(name = "courses_per_week")
    private Long coursesPerWeek;

    public Boolean isActive() {
        boolean result = false;
        if (startDate != null && endDate != null) {
            LocalDate today = LocalDate.now();
            result = !today.isBefore(startDate) && endDate.isAfter(today);
        }
        return result;
    }
}


