package com.ensportive.studentlessons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StudentLessonRepository extends JpaRepository<StudentLessonEntity, Long> {
    @Transactional
    void deleteAllByLessonIdAndExtraStudentFalse(Long lessonId);

    Optional<StudentLessonEntity> findByStudentIdAndLessonId(Long studentId, Long lessonId);
}
