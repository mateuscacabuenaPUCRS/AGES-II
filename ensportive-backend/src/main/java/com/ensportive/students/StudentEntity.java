package com.ensportive.students;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ensportive.auth.UserEntity;
import com.ensportive.enums.Level;
import com.ensportive.licenses.LicenseEntity;
import com.ensportive.requests.RequestEntity;
import com.ensportive.studentlessons.StudentLessonEntity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "students")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "cell_phone", nullable = false, unique = true)
    private String cellPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private LicenseEntity license;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<RequestEntity> requests = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<StudentLessonEntity> lessons = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserEntity user;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
