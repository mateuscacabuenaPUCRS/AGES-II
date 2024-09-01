package com.ensportive.lessonplans;

import com.ensportive.enums.Level;
import com.ensportive.lessons.LessonEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lesson_plans")
public class LessonPlanEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_plan_id")
    private Long id;

    @Column(name = "modality")
    private String modality;

    @Column(name = "warm_up")
    private String warmUp;

    @Column(name = "technique_1")
    private String technique1;

    @Column(name = "technique_2")
    private String technique2;

    @Column(name = "tactic")
    private String tactic;

    @Column(name = "serve")
    private String serve;

    @Column(name = "social")
    private String social;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @OneToOne(mappedBy = "lessonPlan", cascade = CascadeType.ALL)
    private LessonEntity lesson;
}
