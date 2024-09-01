package com.ensportive.lessonplans;

import com.ensportive.lessonplans.dtos.LessonPlanDTO;
import com.ensportive.lessonplans.dtos.LessonPlanRequestDTO;
import com.ensportive.lessons.LessonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface LessonPlanMapper {

    LessonPlanDTO map(LessonPlanEntity lessonPlanEntity);

    LessonPlanRequestDTO mapToRequest(LessonPlanDTO lessonPlansDTO);

    LessonPlanEntity map(LessonPlanDTO lessonPlansDTO);

    LessonPlanEntity map(LessonPlanRequestDTO lessonPlansRequestDTO);

    @Mapping(target = "id", ignore = true)
    LessonPlanEntity mapEntity(LessonPlanEntity lessonPlanEntity);


}