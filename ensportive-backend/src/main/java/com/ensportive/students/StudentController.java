package com.ensportive.students;

import com.ensportive.students.dtos.StudentDTO;
import com.ensportive.students.dtos.StudentRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Get Student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Student", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found by id", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@Parameter(description = "Student Id") @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentDTOById(id));
    }

    @Operation(summary = "Get all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Students", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))})})
    @GetMapping
    public ResponseEntity<List<StudentDTO>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @Operation(summary = "Create a new Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)})
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody StudentRequestDTO request) {
        return new ResponseEntity<>(studentService.create(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found by id", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@Parameter(description = "Student Id") @PathVariable Long id,
                                       @RequestBody StudentRequestDTO request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @Operation(summary = "Patch Student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found by id", content = @Content)})
    @PatchMapping("/{id}")
    public ResponseEntity<Long> patch(@Parameter(description = "Student Id") @PathVariable Long id,
                                      @RequestBody StudentRequestDTO request) {
        return ResponseEntity.ok(studentService.patch(id, request));
    }

    @Operation(summary = "Delete Student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found by id", content = @Content)})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping("/user")
    public ResponseEntity<StudentDTO> getStudentByUsername(@RequestParam String username) {
        return ResponseEntity.ok(studentService.getStudentByUsername(username));
    }

}
