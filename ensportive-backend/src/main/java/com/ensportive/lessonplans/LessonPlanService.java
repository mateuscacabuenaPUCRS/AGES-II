package com.ensportive.lessonplans;

import com.ensportive.lessonplans.dtos.LessonPlanDTO;
import com.ensportive.lessonplans.dtos.LessonPlanRequestDTO;
import com.ensportive.lessons.LessonMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonPlanService {

    @Autowired
    private LessonPlanRepository lessonPlanRepository;
    @Autowired
    private LessonPlanMapper lessonPlanMapper;

    @Autowired
    LessonMapper lessonMapper;

    public List<LessonPlanDTO> findAll() {
        return lessonPlanRepository.findAll().stream()
                .map(lessonPlanMapper::map)
                .collect(Collectors.toList());
    }

    public LessonPlanDTO getById(Long id) {
        return lessonPlanRepository.findById(id)
                .map(lessonPlanMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Lesson Plan not found"));
    }

    public Long create(LessonPlanRequestDTO lessonPlansRequestDTO) {
        LessonPlanEntity lessonPlanEntity = lessonPlanMapper.map(lessonPlansRequestDTO);
        lessonPlanRepository.save(lessonPlanEntity);
        return lessonPlanEntity.getId();
    }

    public Long update(Long id, LessonPlanRequestDTO lessonPlansRequestDTO) {
        LessonPlanEntity lessonPlanEntity = lessonPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson Plan not found"));
        lessonPlanEntity.setModality(lessonPlansRequestDTO.modality());
        lessonPlanEntity.setWarmUp(lessonPlansRequestDTO.warmUp());
        lessonPlanEntity.setTechnique1(lessonPlansRequestDTO.technique1());
        lessonPlanEntity.setTechnique2(lessonPlansRequestDTO.technique2());
        lessonPlanEntity.setTactic(lessonPlansRequestDTO.tactic());
        lessonPlanEntity.setServe(lessonPlansRequestDTO.serve());
        lessonPlanEntity.setSocial(lessonPlansRequestDTO.social());
        lessonPlanEntity.setLevel(lessonPlansRequestDTO.level());


        lessonPlanRepository.save(lessonPlanEntity);
        return id;
    }

    public void delete(Long id) {
        lessonPlanRepository.deleteById(id);
    }
}