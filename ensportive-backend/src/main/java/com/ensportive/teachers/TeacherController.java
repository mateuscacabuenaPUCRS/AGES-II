package com.ensportive.teachers;

import com.ensportive.teachers.dtos.TeacherDTO;
import com.ensportive.teachers.dtos.TeacherRequestDTO;
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

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Operation(summary = "Get all Teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Teachers", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))})})
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> findAll() {
        return ResponseEntity.ok().body(teacherService.findAll());
    }

    @Operation(summary = "Get Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Teacher", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found by id", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getById(@Parameter(description = "Teacher Id") @PathVariable Long id) {
        return ResponseEntity.ok().body(teacherService.getById(id));
    }

    @Operation(summary = "Create Teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)})
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody TeacherRequestDTO teacherDTO) {
        return new ResponseEntity<>(teacherService.create(teacherDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found by id", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@Parameter(description = "Teacher Id") @PathVariable Long id,
                                       @RequestBody TeacherRequestDTO teacherDTO) {
        return ResponseEntity.ok().body(teacherService.update(id, teacherDTO));
    }

    @Operation(summary = "Delete Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found by id", content = @Content)})
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description = "Teacher Id") @PathVariable Long id) {
        teacherService.delete(id);
    }
}
