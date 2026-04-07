package com.sms.student_management.controller;

import com.sms.student_management.dto.StudentDTO;
import com.sms.student_management.dto.StudentResponseDTO;
import com.sms.student_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StudentController {

    private final StudentService studentService;

    @PostMapping()
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        log.info("Received request to create student: {}", dto.getName());
        StudentDTO saved = studentService.saveStudent(dto);
        log.info("Student created with ID: {}", saved.getId());
        System.out.print(saved.getProfileImageUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping
    public ResponseEntity<Page<StudentDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to fetch students, page: {}, size: {}", page, size);
        Page<StudentDTO> students = studentService.getAllStudents(page, size);
        log.info("Fetched {} students", students.getNumberOfElements());
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        log.info("Received request to fetch student with ID: {}", id);
        StudentDTO dto = studentService.getStudentById(id);
        log.info("Student found: {}", dto.getName());
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentDTO dto) {
        log.info("Received request to update student with ID: {}", id);
        StudentDTO updated = studentService.updateStudent(id, dto);
        log.info("Student updated: {}", updated.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.info("Received request to delete student with ID: {}", id);
        studentService.deleteStudent(id);
        log.info("Student deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/profile-image", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "No file provided"
            ));
        }

        String imageUrl = studentService.updateProfileImage(id, file);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Profile image uploaded successfully",
                "url", imageUrl
        ));
    }

    @DeleteMapping("/{id}/profile-image")
    public ResponseEntity<Map<String, String>> deleteProfileImage(@PathVariable Long id) {
        // Call the business logic service
        studentService.deleteProfileImage(id);

        // Return a clean response
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Profile image deleted successfully"
        ));
    }
}