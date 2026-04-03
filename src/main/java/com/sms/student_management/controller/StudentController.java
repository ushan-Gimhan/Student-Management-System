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

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping()
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        log.info("Received request to create student: {}", dto.getName());
        StudentDTO saved = studentService.saveStudent(dto);
        log.info("Student created with ID: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to fetch students, page: {}, size: {}", page, size);
        Page<StudentResponseDTO> students = studentService.getAllStudents(page, size);
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
}