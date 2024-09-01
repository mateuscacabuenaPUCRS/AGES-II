package com.ensportive.students;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    StudentEntity findByUserUsername(String username);

    Optional<StudentEntity> findByUserId(Long userId);

    Optional<StudentEntity> findByEmail(String email);
}
