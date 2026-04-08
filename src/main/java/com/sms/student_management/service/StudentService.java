package com.sms.student_management.service;

import com.sms.student_management.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    // Save a new student
    StudentDTO saveStudent(String studentJson, MultipartFile file);

    // Get all students with pagination
    Page<StudentDTO> getAllStudents(int page, int size);

    // Get a single student by ID
    StudentDTO getStudentById(Long id);

    // Update student information
    StudentDTO updateStudent(Long id, StudentDTO dto);

    // Delete a student by ID
    void deleteStudent(Long id);

    //add profile pic
    String updateProfileImage(Long id, MultipartFile file);

    //deleter profile pic
    void deleteProfileImage(Long id);
}
