package com.ensportive.lessons;

import com.ensportive.courses.CourseEntity;
import com.ensportive.lessonplans.LessonPlanEntity;
import com.ensportive.studentlessons.StudentLessonEntity;
import com.ensportive.teachers.TeacherEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "lessons")
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "spot")
    private Integer spot;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "court")
    private Long court;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private CourseEntity course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @ToString.Exclude
    private TeacherEntity teacher;

    @ManyToOne
    @JoinColumn(name = "second_teacher_id")
    @ToString.Exclude
    private TeacherEntity secondTeacher;

    @OneToOne
    @JoinColumn(name = "lesson_plan_id")
    @ToString.Exclude
    private LessonPlanEntity lessonPlan;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @SQLRestriction("extra_student = false")
    @ToString.Exclude
    private List<StudentLessonEntity> courseStudents = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @SQLRestriction("extra_student = true")
    @ToString.Exclude
    private List<StudentLessonEntity> extraStudents = new ArrayList<>();

}
