package com.ensportive.courses;

import java.util.List;

import com.ensportive.auth.UserRepository;
import com.ensportive.configs.security.JwtService;
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

import com.ensportive.courses.dtos.CourseDTO;
import com.ensportive.courses.dtos.CourseRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Get Course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Course", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getById(@Parameter(description = "Course Id") @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @Operation(summary = "Get all Courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Courses", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDTO.class))})})
    @GetMapping
    public ResponseEntity<List<CourseDTO>> findAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @Operation(summary = "Create a new Course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)})
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CourseRequestDTO request) {
        return new ResponseEntity<>(courseService.create(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@Parameter(description = "Course Id") @PathVariable Long id,
                                       @RequestBody CourseRequestDTO request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @Operation(summary = "Patch Course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
    @PatchMapping("/{id}")
    public ResponseEntity<Long> patch(@Parameter(description = "Course Id") @PathVariable Long id,
                                      @RequestBody CourseRequestDTO courseRequest) {
        return ResponseEntity.ok(courseService.patch(id, courseRequest));
    }

    @Operation(summary = "Delete Course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description = "Course Id") @PathVariable Long id) {
        courseService.delete(id);
    }

    @Operation(summary = "Add students to Course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students added to Course successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
    @PutMapping("/{id}/students")
    public ResponseEntity<Long> addStudentsToCourse(@Parameter(description = "Course Id") @PathVariable Long id, @RequestBody Long[] studentsIds) {
        return ResponseEntity.ok(courseService.addStudentsToCourse(id, studentsIds));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student removed from Course successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found by id", content = @Content)})
    @DeleteMapping("/{id}/students/{studentId}")
    public ResponseEntity<Long> removeStudentFromCourse(@Parameter(description = "Course Id") @PathVariable Long id, @Parameter(description = "Student Id") @PathVariable Long studentId) {
        return ResponseEntity.ok(courseService.removeStudentFromCourse(id, studentId));
    }


}
