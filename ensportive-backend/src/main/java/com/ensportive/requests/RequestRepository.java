package com.ensportive.requests;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
  List<RequestEntity> findByStudentUserId(Long userId);

  Optional<RequestEntity> findByStudentUserIdAndLessonId(Long user, Long lessonId);

  Optional<RequestEntity> findByUserEmail(String email);

  Optional<RequestEntity> findByIdAndStudentUserId(Long id, Long userId);

}