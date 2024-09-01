package com.ensportive.courses;

import com.ensportive.courses.dtos.CourseDTO;
import com.ensportive.courses.dtos.CourseRequestDTO;
import com.ensportive.lessons.LessonService;
import com.ensportive.students.StudentEntity;
import com.ensportive.students.StudentMapper;
import com.ensportive.students.StudentRepository;
import com.ensportive.students.StudentService;
import com.ensportive.teachers.TeacherEntity;
import com.ensportive.teachers.TeacherRepository;
import com.ensportive.utils.Patcher;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonService lessonService;

    public CourseDTO getById(Long id) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found by Id: " + id));
        List<Long> studentIds = mapStudentEntitiesToLong(courseEntity);
        CourseDTO courseDTO = courseMapper.map(courseEntity);
        courseDTO.setStudentsIds(studentIds);
        return courseDTO;
    }

    public List<CourseDTO> findAll() {
        List<CourseEntity> courseEntities = courseRepository.findAll();
        List<CourseDTO> courseDTOS = new ArrayList<>();
        courseEntities
                .forEach(courseEntity -> {
                    List<Long> studentIds = mapStudentEntitiesToLong(courseEntity);
                    CourseDTO courseDTO = courseMapper.map(courseEntity);
                    courseDTO.setStudentsIds(studentIds);
                    courseDTOS.add(courseDTO);
                });
        return courseDTOS;

    }

    public Long create(CourseRequestDTO request) {
        if (request.hour() == null)
            throw new IllegalArgumentException("Hour is required");
        if (request.uniqueLesson() && request.uniqueDate() == null)
            throw new IllegalArgumentException("Date is required for unique lessons");
        else if (!request.uniqueLesson() && request.weekDay() == null)
            throw new IllegalArgumentException("WeekDay is required for recurring lessons");
        CourseEntity courseEntity = courseMapper.map(request);
        if (request.teacherId() != null){
            teacherRepository.findById(request.teacherId()).ifPresent(courseEntity::setTeacher);
        }

        courseRepository.save(courseEntity);
        courseEntity = courseRepository.save(courseEntity);

        if (courseEntity.getUniqueLesson())
            lessonService.createUniqueLesson(courseEntity);
        else
            lessonService.createFirstLessons(courseEntity);

        if (request.studentsIds() != null)
            addStudentsToCourseAndLessons(courseEntity, request.studentsIds());

        return courseEntity.getId();
    }

    public List<Long> mapStudentEntitiesToLong(CourseEntity courseEntity) {
        if (courseEntity.getStudents() == null) {
            return null;
        }
        return courseEntity.getStudents()
                .stream()
                .map(StudentEntity::getId)
                .collect(Collectors.toList());
    }

    public Long update(Long id, CourseRequestDTO courseRequestDTO) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found by id"));

        courseEntity.setLevel(courseRequestDTO.level());
        courseEntity.setWeekDay(courseRequestDTO.weekDay());
        courseEntity.setHour(courseRequestDTO.hour());
        courseEntity.setPlanType(courseRequestDTO.planType());
        courseEntity.setStudentsSize(courseRequestDTO.studentsSize());
        TeacherEntity oldTeacher = courseEntity.getTeacher();
        if (courseRequestDTO.teacherId() != null){
            teacherRepository.findById(courseRequestDTO.teacherId()).ifPresent(courseEntity::setTeacher);
        }

        courseEntity.setSport(courseRequestDTO.sport());
        courseEntity.setCourt(courseRequestDTO.court());

        if (oldTeacher == null || !oldTeacher.getId().equals(courseEntity.getTeacher().getId()))
            lessonService.updateLessonsTeacher(courseEntity);

        courseRepository.save(courseEntity);

        if (courseRequestDTO.studentsIds() != null)
            addStudentsToCourseAndLessons(courseEntity, courseRequestDTO.studentsIds());

        return courseEntity.getId();
    }

    @SneakyThrows
    public Long patch(Long id, CourseRequestDTO courseRequest) {
        CourseDTO existingCourseDTO = this.getById(id);
        Patcher.patcher(existingCourseDTO, courseMapper.mapRequestToDTO(courseRequest));
        courseRepository.save(courseMapper.map(existingCourseDTO));
        return existingCourseDTO.getId();
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public void addStudentsToCourseAndLessons(CourseEntity courseEntity, Long[] longs) {
        List<StudentEntity> students = studentService.findAllById(List.of(longs));
        courseEntity.getStudents().clear();
        students.forEach(student -> {
            if (courseEntity.getStudents().size() < courseEntity.getStudentsSize()) {
                courseEntity.getStudents().add(student);
            }
        });
        courseRepository.save(courseEntity);

        if (!students.isEmpty()) {
             lessonService.addCourseStudentsToLesson(courseEntity.getId());
        }
    }

    public Long addStudentsToCourse(Long id, Long[] studentsIds) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found by id"));

        addStudentsToCourseAndLessons(courseEntity, studentsIds);

        return courseEntity.getId();
    }

    public Long removeStudentFromCourse(Long id, Long studentId) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found by id"));

        StudentEntity studentEntity = studentService.getStudentById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found by id"));

        courseEntity.getStudents().remove(studentEntity);
        courseRepository.save(courseEntity);

        courseEntity.getLessons().forEach(lesson -> {
            lessonService.removeCourseStudentFromLesson(lesson, studentEntity);
        });

        return courseEntity.getId();
    }
}
