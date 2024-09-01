package com.ensportive.teachers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ensportive.auth.UserEntity;
import com.ensportive.enums.Sport;
import com.ensportive.lessons.LessonEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "teachers")
public class TeacherEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "cell_phone", nullable = false, unique = true)
    private String cellPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport", nullable = false)
    private Sport sport;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<LessonEntity> lessons = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}