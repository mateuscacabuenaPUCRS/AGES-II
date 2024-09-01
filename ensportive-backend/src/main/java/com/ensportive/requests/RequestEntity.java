package com.ensportive.requests;

import com.ensportive.enums.RequestStatus;
import com.ensportive.enums.RequestType;
import com.ensportive.lessons.LessonEntity;
import com.ensportive.students.StudentEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "user_email", nullable = true)
    private String userEmail;

    @Column(name = "user_name", nullable = true)
    private String userName;

    @Column(name = "user_phone", nullable = true)
    private String userPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private RequestStatus requestStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = true)
    private LessonEntity lesson;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    private StudentEntity student;

}