package com.ensportive.teachers;

import com.ensportive.teachers.dtos.TeacherDTO;
import com.ensportive.teachers.dtos.TeacherRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherDTO map(TeacherEntity TeacherEntity);

    TeacherRequestDTO mapToRequest(TeacherDTO TeacherDTO);

    TeacherEntity map(TeacherDTO TeacherDTO);

    @Mapping(target = "id", ignore = true)
    TeacherEntity map(TeacherRequestDTO TeacherRequestDTO);
}