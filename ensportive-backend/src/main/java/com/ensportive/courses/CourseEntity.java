package com.ensportive.courses;

import com.ensportive.enums.Level;
import com.ensportive.enums.PlanType;
import com.ensportive.enums.Sport;
import com.ensportive.lessons.LessonEntity;
import com.ensportive.students.StudentEntity;
import com.ensportive.teachers.TeacherEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport", nullable = false)
    private Sport sport;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    @Column(name = "students_size", nullable = false)
    private Integer studentsSize;

    @Column(name = "unique_lesson", nullable = false)
    private Boolean uniqueLesson;

    @Column(name = "date", nullable = true)
    private LocalDate uniqueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day", nullable = true)
    private DayOfWeek weekDay;

    @Column(name = "hour", nullable = false)
    private LocalTime hour;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;

    @Column(name = "court")
    private Long court;

    @ManyToMany
    @JoinTable(name = "courses_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<StudentEntity> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<LessonEntity> lessons = new ArrayList<>();
}
