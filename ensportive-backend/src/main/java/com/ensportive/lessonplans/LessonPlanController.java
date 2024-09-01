package com.ensportive.lessonplans;

import com.ensportive.lessonplans.dtos.LessonPlanDTO;
import com.ensportive.lessonplans.dtos.LessonPlanRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/lessonPlan")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LessonPlanController {

    @Autowired
    private LessonPlanService lessonPlanService;

    @Operation(summary = "Get all Lessons Plans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Lessons Plans", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LessonPlanDTO.class))})})
    @GetMapping
    public ResponseEntity<List<LessonPlanDTO>> findAll() {
        return ResponseEntity.ok().body(lessonPlanService.findAll());
    }

    @Operation(summary = "Get Lesson Plan by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Lesson Plan", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LessonPlanDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson Plan not found by id", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<LessonPlanDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonPlanService.getById(id));
    }

    @Operation(summary = "Create Lesson Plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson Plan created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LessonPlanDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)})
    @PostMapping
    public ResponseEntity<Long> save(@RequestBody LessonPlanRequestDTO lessonPlansDTO) {
        return new ResponseEntity<>(lessonPlanService.create(lessonPlansDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Lesson Plan by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson Plan updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson Plan not found by id", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody LessonPlanRequestDTO lessonPlansDTO) {
        return ResponseEntity.ok().body(lessonPlanService.update(id, lessonPlansDTO));
    }

    @Operation(summary = "Delete Lesson Plan by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson Plan deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson Plan not found by id", content = @Content)})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        lessonPlanService.delete(id);
    }
}
