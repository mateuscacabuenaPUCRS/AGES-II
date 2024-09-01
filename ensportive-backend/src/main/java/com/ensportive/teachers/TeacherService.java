package com.ensportive.teachers;

import java.util.List;
import java.util.stream.Collectors;

import com.ensportive.teachers.dtos.TeacherDTO;
import com.ensportive.teachers.dtos.TeacherRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherMapper teacherMapper;

    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::map)
                .collect(Collectors.toList());
    }

    public TeacherDTO getById(Long id) {
        return teacherRepository.findById(id)
                .map(teacherMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found by Id: " + id));
    }

    public Long create(TeacherRequestDTO teacherDTO) {
        TeacherEntity teacherEntity = teacherMapper.map(teacherDTO);
        teacherRepository.save(teacherEntity);
        return teacherEntity.getId();
    }

    public Long update(Long id, TeacherRequestDTO teacherDTO){
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found by Id: " + id));
        teacherEntity.setEmail(teacherDTO.email());
        teacherEntity.setCellPhone(teacherDTO.cellPhone());
        teacherEntity.setName(teacherDTO.name());
        teacherEntity.setSport(teacherDTO.sport());
        teacherRepository.save(teacherEntity);
        return teacherEntity.getId();
    }

    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }
}