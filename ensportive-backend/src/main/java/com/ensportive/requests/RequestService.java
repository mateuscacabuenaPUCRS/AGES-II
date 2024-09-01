package com.ensportive.requests;

import com.ensportive.enums.RequestStatus;
import com.ensportive.enums.RequestType;
import com.ensportive.lessons.LessonEntity;
import com.ensportive.lessons.LessonRepository;
import com.ensportive.requests.dtos.AbsenceRequestDTO;
import com.ensportive.requests.dtos.RegisterRequestDTO;
import com.ensportive.requests.dtos.RequestDTO;
import com.ensportive.studentlessons.StudentLessonEntity;
import com.ensportive.students.StudentEntity;
import com.ensportive.students.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private LessonRepository lessonRepository;

    public List<RequestDTO> getAllRequests() {
        List<RequestEntity> requests = requestRepository.findAll();
        return requestMapper.map(requests);
    }

    public RequestDTO getRequestById(Long id) {
        return requestRepository.findById(id)
                .map(requestMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Request not found by id"));
    }

    public RequestDTO getRequestById(Long id, Long userId) {
        return requestRepository.findByIdAndStudentUserId(id, userId)
                .map(requestMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Request not found by id"));
    }

    public Long createAbsenceRequest(AbsenceRequestDTO requestDTO, Long userId) {
        RequestEntity requestEntity = requestRepository.findByStudentUserIdAndLessonId(userId, requestDTO.lessonId())
                .orElse(new RequestEntity());

        StudentEntity student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        LessonEntity lesson = lessonRepository.findById(requestDTO.lessonId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        StudentLessonEntity studentLesson = lesson.getCourseStudents().stream()
                .filter(sl -> sl.getStudent().getId().equals(student.getId()))
                .findFirst().get();

        if (studentLesson.getAttendance() != StudentLessonEntity.Attendance.PRESENT) {
            studentLesson.setAttendance(StudentLessonEntity.Attendance.PRESENT);
            lessonRepository.save(lesson);

            if (requestEntity.getId() != null)
                requestRepository.delete(requestEntity);
            return -1L;
        }
        else {
            studentLesson.setAttendance(StudentLessonEntity.Attendance.ABSENT);
            lessonRepository.save(lesson);

            requestEntity.setStudent(student);
            requestEntity.setLesson(lesson);
            requestEntity.setRequestStatus(RequestStatus.REQUESTED);
            requestEntity.setCreationDate(LocalDateTime.now());
            requestEntity.setRequestType(RequestType.ABSENCE);

            if (requestDTO.description() != null) {
                requestEntity.setDescription(requestDTO.description());
            } else {
                requestEntity.setDescription("");
            }
            requestEntity = requestRepository.save(requestEntity);
            return requestEntity.getId();
        }
    }

    public Long createRegistrationRequest(RegisterRequestDTO requestDTO) {
        if (requestDTO.userEmail() == null || requestDTO.userName() == null || requestDTO.userPhone() == null) {
            throw new IllegalArgumentException("Request data is not valid");
        }

        if (requestRepository.findByUserEmail(requestDTO.userEmail()).isPresent()) {
            throw new IllegalArgumentException("Request with this email already exists");
        }

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setUserEmail(requestDTO.userEmail());
        requestEntity.setUserName(requestDTO.userName());
        requestEntity.setUserPhone(requestDTO.userPhone());
        requestEntity.setRequestType(RequestType.REGISTER);
        requestEntity.setRequestStatus(RequestStatus.REQUESTED);

        if (requestDTO.description() != null) {
            requestEntity.setDescription(requestDTO.description());
        } else {
            requestEntity.setDescription("");
        }
        requestEntity.setCreationDate(LocalDateTime.now());
        requestEntity = requestRepository.save(requestEntity);
        return requestEntity.getId();
    }

    public void delete(Long id) {
        requestRepository.deleteById(id);
    }

    public List<RequestDTO> getRequestsByUserId(Long userId) {
        return requestRepository.findByStudentUserId(userId).stream()
                .map(requestMapper::map)
                .collect(Collectors.toList());
    }

    public void approveRequest(Long id) {
        RequestEntity request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found by id"));

        if (request.getRequestStatus() != RequestStatus.REQUESTED) {
            throw new IllegalArgumentException("Request is already filled");
        }

        request.setRequestStatus(RequestStatus.FILLED);
        requestRepository.save(request);
    }

    public void denyRequest(Long id) {
        RequestEntity request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found by id"));

        if (request.getRequestStatus() != RequestStatus.REQUESTED) {
            throw new IllegalArgumentException("Request is already filled");
        }

        request.setRequestStatus(RequestStatus.DENIED);
        requestRepository.save(request);
    }
}