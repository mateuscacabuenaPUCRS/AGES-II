package com.ensportive.requests.dtos;

import com.ensportive.lessons.dtos.LessonDTO;
import com.ensportive.students.dtos.StudentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDTO {
    @Schema(name = "id", example = "1")
    private Long id;

    @Schema(name = "description", example = "Solicitação de Cancelamento")
    private String description;

    @Schema(name = "requestStatus", example = "FILLED")
    private String requestStatus;

    @Schema(name = "requestType", example = "ABSENCE")
    private String requestType;

    @Schema(name = "lesson", implementation = LessonDTO.class)
    private LessonDTO lesson;

    @Schema(name = "student", implementation = StudentDTO.class)
    private StudentDTO student;

    @Schema(name = "userEmail", example = "user@email.com")
    private String userEmail;

    @Schema(name = "userName", example = "User Name")
    private String userName;

    @Schema(name = "creationDate", example = "2021-09-01T00:00:00")
    private LocalDateTime creationDate;
}
