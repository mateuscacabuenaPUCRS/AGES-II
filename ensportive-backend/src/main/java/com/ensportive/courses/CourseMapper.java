package com.ensportive.courses;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ensportive.courses.dtos.CourseDTO;
import com.ensportive.courses.dtos.CourseRequestDTO;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO map(CourseEntity courseEntity);

    CourseRequestDTO mapToRequest(CourseDTO courseDTO);

    CourseEntity map(CourseDTO courseDTO);

    @Mapping(target = "id", ignore = true)
    CourseEntity map(CourseRequestDTO courseRequestDTO);

    @Mapping(target = "id", ignore = true)
    CourseDTO mapRequestToDTO(CourseRequestDTO courseRequestDTO);
}