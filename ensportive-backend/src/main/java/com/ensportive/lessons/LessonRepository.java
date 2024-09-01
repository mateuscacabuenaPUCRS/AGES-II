package com.ensportive.lessons;

import java.util.List;
import java.util.Optional;

import com.ensportive.courses.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    List<LessonEntity> findByCourseStudentsStudentUserIdOrExtraStudentsStudentUserId(Long id, Long id2);

    List<LessonEntity> findByTeacherId(Long id);

    Optional<LessonEntity> findTopByCourseOrderByDateDesc(CourseEntity course);

    List<LessonEntity> findByCourseId(Long courseId);
}
