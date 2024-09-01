package com.ensportive.lessons;

import com.ensportive.courses.CourseEntity;
import com.ensportive.courses.CourseRepository;
import com.ensportive.lessonplans.LessonPlanEntity;
import com.ensportive.lessonplans.LessonPlanMapper;
import com.ensportive.lessonplans.LessonPlanRepository;
import com.ensportive.lessons.dtos.LessonDTO;
import com.ensportive.lessons.dtos.LessonRequestDTO;
import com.ensportive.studentlessons.StudentLessonEntity;
import com.ensportive.studentlessons.StudentLessonRepository;
import com.ensportive.students.StudentEntity;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private LessonPlanRepository lessonPlanRepository;
    @Autowired
    private LessonPlanMapper lessonPlanMapper;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentLessonRepository studentLessonRepository;

    public List<LessonDTO> findAll() {
        return lessonRepository.findAll().stream()
                .map(lesson -> lessonMapper.map(lesson))
                .collect(Collectors.toList());
    }

    public LessonDTO getById(Long id) {
        return lessonRepository.findById(id)
                .map(lesson -> lessonMapper.map(lesson))
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
    }

    public LessonDTO create(LessonRequestDTO lessonDTO) {
        LessonEntity lessonEntity = lessonMapper.map(lessonDTO);
        TeacherEntity teacher = teacherRepository.findById(lessonDTO.teacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        CourseEntity course = courseRepository.findById(lessonDTO.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        LessonPlanEntity lessonPlan = lessonPlanRepository.findById(lessonDTO.lessonPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson plan not found"));
        LessonPlanEntity newLessonPlan = lessonPlanMapper.mapEntity(lessonPlan);
        lessonPlanRepository.save(newLessonPlan);

        if (lessonDTO.secondTeacherId() != null) {
            teacherRepository.findById(lessonDTO.secondTeacherId()).ifPresent(lessonEntity::setSecondTeacher);
        }

        lessonEntity.setTeacher(teacher);
        lessonEntity.setCourse(course);
        lessonEntity.setLessonPlan(newLessonPlan);

        return this.lessonMapper.map(lessonRepository.save(lessonEntity));
    }

    public Long update(Long id, LessonRequestDTO lessonDTO) {
        LessonEntity lessonEntity = lessonRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (lessonDTO.teacherId() != null) {
            teacherRepository.findById(lessonDTO.teacherId()).ifPresent(lessonEntity::setTeacher);
        }

        if (lessonDTO.secondTeacherId() != null) {
            teacherRepository.findById(lessonDTO.secondTeacherId()).ifPresent(lessonEntity::setSecondTeacher);
        }

        lessonEntity.setDescription(lessonDTO.description());
        lessonEntity.setCourt(lessonDTO.court());
        lessonEntity.setSpot(lessonDTO.spot());
        if(lessonDTO.lessonPlanId() != null) {
            LessonPlanEntity lessonPlanEntity = lessonPlanRepository.findById(lessonDTO.lessonPlanId()).orElseThrow(EntityNotFoundException::new);
            lessonEntity.setLessonPlan(lessonPlanEntity);
        }
        lessonRepository.save(lessonEntity);
        return id;
    }

    @SneakyThrows
    public Long patch(Long id, LessonRequestDTO lessonRequest) {
        LessonDTO existingLessonDTO = this.getById(id);
        Patcher.patcher(existingLessonDTO, lessonMapper.mapRequestToDTO(lessonRequest));
        lessonRepository.save(lessonMapper.map(existingLessonDTO));
        return id;
    }

    public Long delete(Long id) {
        lessonRepository.deleteById(id);
        return id;
    }

    public List<LessonDTO> getLessonsByUserId(Long userId) {
        return lessonRepository.findByCourseStudentsStudentUserIdOrExtraStudentsStudentUserId(userId, userId).stream()
                .map(lesson -> lessonMapper.map(lesson))
                .collect(Collectors.toList());

    }

    public List<LessonDTO> getLessonsByTeacherId(Long teacherId) {
        return lessonRepository.findByTeacherId(teacherId).stream()
                .map(lesson -> lessonMapper.map(lesson))
                .collect(Collectors.toList());
    }

    public void addExtraStudentToLesson(LessonEntity lessonEntity, StudentEntity studentEntity) {
        boolean studentExistsInLesson = lessonEntity.getCourseStudents().stream().anyMatch(sl -> sl.getStudent().equals(studentEntity)) ||
                lessonEntity.getExtraStudents().stream().anyMatch(sl -> sl.getStudent().equals(studentEntity));

        if (!studentExistsInLesson) {
            StudentLessonEntity studentLesson = new StudentLessonEntity();
            studentLesson.setLesson(lessonEntity);
            studentLesson.setStudent(studentEntity);
            studentLesson.setExtraStudent(true);

            lessonEntity.getExtraStudents().add(studentLesson);

            lessonRepository.save(lessonEntity);
        }
    }

    public void addExtraStudentsToLesson(Long idLesson, List<Long> students) {
        LessonEntity lessonEntity = lessonRepository.findById(idLesson).orElseThrow(() -> new EntityNotFoundException("Lesson not found by id"));
        List<StudentEntity> studentEntities = studentRepository.findAllById(students);
        studentEntities.forEach(studentEntity -> addExtraStudentToLesson(lessonEntity, studentEntity));
    }

    public void addCourseStudentsToLesson(Long courseId) {
        List<LessonEntity> lessons = lessonRepository.findByCourseId(courseId);

        lessons.forEach(lessonEntity -> {
            studentLessonRepository.deleteAllByLessonIdAndExtraStudentFalse(lessonEntity.getId());
            if (!lessonEntity.getDate().isBefore(LocalDateTime.now())) {
                lessonEntity.getCourse().getStudents().forEach(student -> {
                        StudentLessonEntity studentLesson = new StudentLessonEntity();
                        studentLesson.setAttendance(StudentLessonEntity.Attendance.PRESENT);
                        studentLesson.setLesson(lessonEntity);
                        studentLesson.setStudent(student);
                        lessonEntity.getCourseStudents().add(studentLesson);
                });
                lessonRepository.save(lessonEntity);
            }
        });
    }

    public void createFirstLessons(CourseEntity courseEntity) {
        List<LessonEntity> lessons = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        LocalTime courseTime = courseEntity.getHour();
        DayOfWeek courseDay = courseEntity.getWeekDay();
        LocalDate nextLessonDate = startDate.with(courseDay);

        if (nextLessonDate.isBefore(startDate)) {
            nextLessonDate = nextLessonDate.plusWeeks(1);
        }

        for (int i = 0; i < 12; i++) {
            LocalDateTime lessonDateTime = nextLessonDate.atTime(courseTime);
            LessonEntity lesson = new LessonEntity();
            lesson.setCourse(courseEntity);
            lesson.setDate(lessonDateTime);
            lesson.setSpot(courseEntity.getStudentsSize());
            if (courseEntity.getTeacher() != null) {
                lesson.setTeacher(courseEntity.getTeacher());
            }
            lessons.add(lesson);
            nextLessonDate = nextLessonDate.plusWeeks(1);
        }

        lessonRepository.saveAll(lessons);

    }

    public void createUniqueLesson(CourseEntity courseEntity) {
        LessonEntity lesson = new LessonEntity();
        lesson.setCourse(courseEntity);
        lesson.setDate(courseEntity.getUniqueDate().atTime(courseEntity.getHour()));
        lesson.setSpot(courseEntity.getStudentsSize());
        if (courseEntity.getTeacher() != null) {
            lesson.setTeacher(courseEntity.getTeacher());
        }
        courseEntity.getLessons().add(lesson);
        lessonRepository.save(lesson);
    }

    public void updateLessonsTeacher(CourseEntity courseEntity) {
        courseEntity.getLessons().forEach(lessonEntity -> {
            if (lessonEntity.getDate().isAfter(LocalDateTime.now()))
                lessonEntity.setTeacher(courseEntity.getTeacher());
        });
    }

    public void changeTeacher(Long idLesson, Long idTeacher) {
        LessonEntity lessonEntity = lessonRepository.findById(idLesson).orElseThrow(() -> new EntityNotFoundException("Lesson not found by id"));
        TeacherEntity teacherEntity = teacherRepository.findById(idTeacher).orElseThrow(() -> new EntityNotFoundException("Teacher not found by id"));

        lessonEntity.setTeacher(teacherEntity);
        lessonRepository.save(lessonEntity);
    }

    public void changeSecondaryTeacher(Long idLesson, Long idTeacher) {
        LessonEntity lessonEntity = lessonRepository.findById(idLesson).orElseThrow(() -> new EntityNotFoundException("Lesson not found by id"));
        TeacherEntity teacherEntity = teacherRepository.findById(idTeacher).orElseThrow(() -> new EntityNotFoundException("Teacher not found by id"));

        lessonEntity.setSecondTeacher(teacherEntity);
        lessonRepository.save(lessonEntity);
    }

    public void removeExtraStudentFromLesson(Long idLesson, Long idStudent) {
        LessonEntity lessonEntity = lessonRepository.findById(idLesson).orElseThrow(() -> new EntityNotFoundException("Lesson not found by id"));
        StudentEntity studentEntity = studentRepository.findById(idStudent).orElseThrow(() -> new EntityNotFoundException("Student not found by id"));

        studentLessonRepository.findByStudentIdAndLessonId(idStudent, idLesson)
                .ifPresent(studentLessonEntity -> studentLessonRepository.delete(studentLessonEntity));

    }

    public void removeCourseStudentFromLesson(LessonEntity lesson, StudentEntity studentEntity) {
        lesson.getCourseStudents()
                .removeIf(sl -> sl.getStudent().equals(studentEntity));
        lessonRepository.save(lesson);
    }
}
