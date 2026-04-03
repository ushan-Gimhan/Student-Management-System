package com.sms.student_management.service;

import com.sms.student_management.dto.StudentDTO;
import com.sms.student_management.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test") // Use application-test.properties with H2 database
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService service;

    @Test
    void testAddAndFetchStudent() {
        // Add student
        StudentDTO dto = new StudentDTO();
        dto.setName("Nimal Silva");
        dto.setEmail("nimal@example.lk");
        dto.setCourse("IT");

        StudentDTO saved = service.saveStudent(dto);

        // Fetch student by ID
        StudentDTO fetched = service.getStudentById(saved.getId());

        assertEquals("Nimal Silva", fetched.getName());
        assertEquals("nimal@example.lk", fetched.getEmail());
        assertEquals("IT", fetched.getCourse());
    }

    @Test
    void testUpdateStudent() {
        // Add a student
        StudentDTO dto = new StudentDTO();
        dto.setName("Kamal Perera");
        dto.setEmail("kamal@example.lk");
        dto.setCourse("CS");
        StudentDTO saved = service.saveStudent(dto);

        // Update student
        dto.setName("Kamal Updated");
        dto.setEmail("kamal.updated@example.lk");
        StudentDTO updated = service.updateStudent(saved.getId(), dto);

        assertEquals("Kamal Updated", updated.getName());
        assertEquals("kamal.updated@example.lk", updated.getEmail());
    }

    @Test
    void testDeleteStudent() {
        // Add a student
        StudentDTO dto = new StudentDTO();
        dto.setName("Sunil Fernando");
        dto.setEmail("sunil@example.lk");
        dto.setCourse("IT");
        StudentDTO saved = service.saveStudent(dto);

        // Delete student
        service.deleteStudent(saved.getId());

        // Verify deletion
        assertThrows(ResourceNotFoundException.class, () -> service.getStudentById(saved.getId()));
    }

}
