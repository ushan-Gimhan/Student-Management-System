package com.sms.student_management.service;

import com.sms.student_management.dto.StudentDTO;
import com.sms.student_management.dto.StudentResponseDTO;
import org.springframework.data.domain.Page;

public interface StudentService {

    // Save a new student
    StudentDTO saveStudent(StudentDTO dto);

    // Get all students with pagination
    Page<StudentResponseDTO> getAllStudents(int page, int size);

    // Get a single student by ID
    StudentDTO getStudentById(Long id);

    // Update student information
    StudentDTO updateStudent(Long id, StudentDTO dto);

    // Delete a student by ID
    void deleteStudent(Long id);
}
