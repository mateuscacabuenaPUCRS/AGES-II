package com.ensportive.lessons;

import java.util.List;

import com.ensportive.configs.security.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensportive.lessons.dtos.LessonDTO;
import com.ensportive.lessons.dtos.LessonRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LessonController {

        @Autowired
        private LessonService lessonService;

        @Autowired
        private AuthenticationUtils authenticationUtils;

        @Operation(summary = "Get Lesson by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found Lesson", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Lesson not found by id", content = @Content) })
        @GetMapping("/{id}")
        public ResponseEntity<LessonDTO> getById(@Parameter(description = "Lesson Id") @PathVariable Long id) {
                return ResponseEntity.ok(lessonService.getById(id));
        }

        @Operation(summary = "Get all Lesson")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of Lesson", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class)) }) })
        @GetMapping
        public ResponseEntity<List<LessonDTO>> findAll() {
                if (authenticationUtils.hasRole("ADMIN"))
                        return ResponseEntity.ok(lessonService.findAll());
                return ResponseEntity.ok(lessonService.getLessonsByUserId(authenticationUtils.getAuthenticatedUserId()));
        }

        @Operation(summary = "Create a new Lesson")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Lesson created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
        @PostMapping
        public ResponseEntity<LessonDTO> create(@RequestBody LessonRequestDTO request) {
                return new ResponseEntity<>(lessonService.create(request), HttpStatus.CREATED);
        }

        @Operation(summary = "Update Lesson by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lesson updated successfully", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Lesson not found by id", content = @Content) })
        @PutMapping("/{id}")
        public ResponseEntity<Long> update(@Parameter(description = "Lesson Id") @PathVariable Long id,
                                           @RequestBody LessonRequestDTO request) {
                return ResponseEntity.ok(lessonService.update(id, request));
        }

        @Operation(summary = "Patch Lesson by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lesson updated successfully", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Lesson not found by id", content = @Content) })
        @PatchMapping("/{id}")
        public ResponseEntity<Long> patch(@Parameter(description = "Lesson Id") @PathVariable Long id,
                        @RequestBody LessonRequestDTO lessonRequest) {
                return ResponseEntity.ok(lessonService.patch(id, lessonRequest));
        }

        @Operation(summary = "Delete Lesson by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lesson deleted successfully", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Lesson not found by id", content = @Content) })
        @DeleteMapping("/{id}")
        public ResponseEntity<Long> delete(@Parameter(description = "Lesson Id") @PathVariable Long id) {
                return ResponseEntity.ok(lessonService.delete(id));
        }

        @Operation(summary = "Get Lessons by User id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of Lesson", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class)) }) })
        @GetMapping("/student/{userId}")
        public List<LessonDTO> getLessonsByUserId(
                        @Parameter(description = "User Id") @PathVariable Long userId) {
                return lessonService.getLessonsByUserId(userId);
        }

        @Operation(summary = "Get Lessons by Teacher id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of Lesson", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class)) }) })
        @GetMapping("/teachers/{teacherId}")
        public List<LessonDTO> getLessonsByTeacherId(
                        @Parameter(description = "Teacher Id") @PathVariable Long teacherId) {
                return lessonService.getLessonsByTeacherId(teacherId);
        }

        @Operation(summary = "Add students to lesson")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Students added successfully", content = @Content),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
        @PutMapping("/{idLesson}/students")
        public void addExtraStudentsToLesson(@PathVariable Long idLesson, @RequestBody List<Long> students) {
                lessonService.addExtraStudentsToLesson(idLesson, students);
        }

        @Operation(summary = "Change primary teacher of lesson")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Teacher changed successfully", content = @Content),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
        @PutMapping("/{idLesson}/teacher/{idTeacher}")
        public void changeTeacher(@PathVariable Long idLesson, @PathVariable Long idTeacher) {
                lessonService.changeTeacher(idLesson, idTeacher);
        }

        @Operation(summary = "Change secondary teacher of lesson")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Teacher changed successfully", content = @Content),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
        @PutMapping("/{idLesson}/secondaryTeacher/{idTeacher}")
        public void changeSecondaryTeacher(@PathVariable Long idLesson, @PathVariable Long idTeacher) {
                lessonService.changeSecondaryTeacher(idLesson, idTeacher);
        }

        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Student removed successfully", content = @Content),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
        @DeleteMapping("/{idLesson}/students/{idStudent}")
        public void removeExtraStudentFromLesson(@PathVariable Long idLesson, @PathVariable Long idStudent) {
                lessonService.removeExtraStudentFromLesson(idLesson, idStudent);
        }
}
