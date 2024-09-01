package com.ensportive.lessons;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ensportive.lessons.dtos.LessonDTO;
import com.ensportive.lessons.dtos.LessonRequestDTO;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonDTO map(LessonEntity lessonEntity);

    LessonRequestDTO mapToRequest(LessonDTO lessonDTO);

    LessonEntity map(LessonDTO lessonDTO);

    @Mapping(target = "id", ignore = true)
    LessonEntity map(LessonRequestDTO lessonRequestDTO);

    LessonDTO mapRequestToDTO(LessonRequestDTO lessonRequestDTO);
}
