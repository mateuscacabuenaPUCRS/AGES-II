package com.ensportive.studentlessons;

import com.ensportive.studentlessons.dtos.StudentLessonDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentLessonMapper {
    public StudentLessonDTO map(StudentLessonEntity studentLessonEntity);
}
