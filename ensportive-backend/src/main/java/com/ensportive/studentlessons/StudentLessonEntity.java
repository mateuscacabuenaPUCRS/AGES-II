package com.ensportive.studentlessons;

import com.ensportive.lessons.LessonEntity;
import com.ensportive.students.StudentEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "student_lessons")
public class StudentLessonEntity {

    public enum Attendance {
        PRESENT, ABSENT, JUSTIFIED;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_lesson_id")
    private Long id;

    @Column(name = "attendance")
    @Enumerated(EnumType.STRING)
    private Attendance attendance;

    @Column(name = "extra_student")
    private Boolean extraStudent = false;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @ToString.Exclude
    private LessonEntity lesson;
}
