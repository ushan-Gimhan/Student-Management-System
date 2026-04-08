package com.sms.student_management.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.student_management.dto.StudentDTO;
import com.sms.student_management.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService service;

    // Required to convert DTO to String for the saveStudent method
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddAndFetchStudent() throws Exception {
        StudentDTO dto = new StudentDTO();
        dto.setName("Nimal Silva");
        dto.setEmail("nimal@example.lk");
        dto.setCourse("IT");

        // 1. Convert DTO to JSON String for saveStudent
        String studentJson = objectMapper.writeValueAsString(dto);

        // 2. Pass String and null (for file)
        StudentDTO saved = service.saveStudent(studentJson, null);

        StudentDTO fetched = service.getStudentById(saved.getId());

        assertEquals("Nimal Silva", fetched.getName());
        assertEquals("nimal@example.lk", fetched.getEmail());
    }

    @Test
    void testUpdateStudent() throws Exception {
        // --- Setup: Save a student first ---
        StudentDTO dto = new StudentDTO();
        dto.setName("Kamal Perera");
        dto.setEmail("kamal@example.lk");
        dto.setCourse("CS");

        String studentJson = objectMapper.writeValueAsString(dto);
        StudentDTO saved = service.saveStudent(studentJson, null);

        // --- Action: Update the student ---
        // Create a DTO for updating (only the fields you want to change)
        StudentDTO updateDto = new StudentDTO();
        updateDto.setName("Kamal Updated");
        updateDto.setEmail("kamal.updated@example.lk");
        updateDto.setCourse("CS");

        // FIXED: Passing the DTO object directly, NOT a String
        StudentDTO updated = service.updateStudent(saved.getId(), updateDto);

        assertEquals("Kamal Updated", updated.getName());
        assertEquals("kamal.updated@example.lk", updated.getEmail());
    }

    @Test
    void testDeleteStudent() throws Exception {
        StudentDTO dto = new StudentDTO();
        dto.setName("Sunil Fernando");
        dto.setEmail("sunil@example.lk");
        dto.setCourse("Business");

        String studentJson = objectMapper.writeValueAsString(dto);
        StudentDTO saved = service.saveStudent(studentJson, null);

        service.deleteStudent(saved.getId());

        assertThrows(ResourceNotFoundException.class, () -> service.getStudentById(saved.getId()));
    }
}
