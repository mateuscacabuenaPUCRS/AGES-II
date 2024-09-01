package com.ensportive.students;

import com.ensportive.licenses.LicenseEntity;
import com.ensportive.licenses.LicenseMapper;
import com.ensportive.licenses.LicenseRepository;
import com.ensportive.licenses.dtos.LicenseDTO;
import com.ensportive.students.dtos.StudentDTO;
import com.ensportive.students.dtos.StudentRequestDTO;
import com.ensportive.utils.Patcher;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private LicenseMapper licenseMapper;

    public StudentDTO getStudentDTOById(Long id) {
        StudentEntity studentEntity = getStudentById(id).orElseThrow(() -> new EntityNotFoundException("Student not found by Id: " + id));
        LicenseDTO licenseDTO = licenseRepository.findById(studentEntity.getLicense().getId()).map(licenseMapper::map).orElseThrow(EntityNotFoundException::new);
        StudentDTO studentDTO = studentMapper.map(studentEntity);
        studentDTO.setLicense(licenseDTO);
        return studentDTO;
    }

    public Optional<StudentEntity> getStudentById(Long id) {
     return studentRepository.findById(id);
    }

    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream().map(studentMapper::map).toList();
    }

    public Long create(StudentRequestDTO request) {
        StudentEntity studentEntity = studentMapper.map(request);

        if (request.license() == null || request.license().active() == null) {
            throw new EntityNotFoundException("Licença precisa ser informada");
        }

        LicenseEntity license = new LicenseEntity();

        if (request.license().active()) {
            if (request.license().startDate() == null || request.license().endDate() == null) {
                throw new EntityNotFoundException("Data de início e término precisam ser informadas");
            }

            if (!request.license().startDate().isBefore(request.license().endDate())) {
                throw new EntityNotFoundException("Data de início precisa ser anterior a data de término");
            }
            if (request.license().startDate().isBefore(LocalDate.now())) {
                throw new EntityNotFoundException("Data de início precisa ser posterior a data atual");
            }

            license.setStartDate(request.license().startDate());
            license.setEndDate(request.license().endDate());
            license.setPlanType(request.license().planType());
            license.setFrequency(request.license().frequency());
            license.setSport(request.license().sport());
            license.setCoursesPerWeek(request.license().coursesPerWeek());
        }

        studentEntity.setLicense(license);
        studentRepository.save(studentEntity);
        return studentEntity.getId();
    }

    public Long update(Long id, StudentRequestDTO request) {
        StudentEntity studentEntity = studentRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        LicenseEntity licenseEntity = studentEntity.getLicense();

        if (request.license().active()) {
            if (request.license().startDate() == null || request.license().endDate() == null) {
                throw new EntityNotFoundException("Data de início e término precisam ser informadas");
            }

            if (!request.license().startDate().isBefore(request.license().endDate())) {
                throw new EntityNotFoundException("Data de início precisa ser anterior a data de término");
            }
            if (request.license().startDate().isBefore(LocalDate.now())) {
                throw new EntityNotFoundException("Data de início precisa ser posterior a data atual");
            }

            licenseEntity.setStartDate(request.license().startDate());
            licenseEntity.setEndDate(request.license().endDate());
            licenseEntity.setPlanType(request.license().planType());
            licenseEntity.setFrequency(request.license().frequency());
            licenseEntity.setSport(request.license().sport());
            licenseEntity.setCoursesPerWeek(request.license().coursesPerWeek());
        }
        else {
            licenseEntity.setStartDate(null);
            licenseEntity.setEndDate(null);
        }

        studentEntity.setLevel(request.level());
        studentEntity.setName(request.name());
        studentEntity.setEmail(request.email());
        studentEntity.setCellPhone(request.cellPhone());
        studentEntity.setLicense(licenseEntity);
        studentRepository.save(studentEntity);
        return studentEntity.getId();
    }

    @SneakyThrows
    public Long patch(Long id, StudentRequestDTO studentRequest) {
        StudentDTO existingStudentDTO = this.getStudentDTOById(id);
        Patcher.patcher(existingStudentDTO, studentMapper.mapRequestToDTO(studentRequest));
        studentRepository.save(studentMapper.map(existingStudentDTO));
        return existingStudentDTO.getId();
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
    public List <StudentEntity> findAllById(List<Long> longs) {
        return studentRepository.findAllById(longs);
    }

    public StudentDTO getStudentByUsername(String username) {
        return studentMapper.map(studentRepository.findByUserUsername(username));
    }

}
