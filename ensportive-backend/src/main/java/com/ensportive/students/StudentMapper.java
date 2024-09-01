package com.ensportive.students;

import com.ensportive.students.dtos.StudentDTO;
import com.ensportive.students.dtos.StudentRequestDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

  @Mapping(target = "username", source = "user.username")
  StudentDTO map(StudentEntity studentEntity);

  StudentEntity map(StudentDTO studentDTO);

  StudentEntity map(StudentRequestDTO student);

  StudentDTO mapRequestToDTO(StudentRequestDTO studentRequest);

}