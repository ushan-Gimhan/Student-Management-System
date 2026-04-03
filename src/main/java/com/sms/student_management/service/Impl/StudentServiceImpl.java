package com.sms.student_management.service.Impl;

import com.sms.student_management.dto.StudentDTO;
import com.sms.student_management.dto.StudentResponseDTO;
import com.sms.student_management.entity.Student;
import com.sms.student_management.exception.ResourceNotFoundException;
import com.sms.student_management.mapper.StudentMapper;
import com.sms.student_management.repository.StudentRepository;
import com.sms.student_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;

    @Override
    public StudentDTO saveStudent(StudentDTO dto) {
        log.info("Saving new student: {}", dto.getName());
        Student student = mapper.toEntity(dto);
        Student saved = repository.save(student);
        log.info("Student saved with ID: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public Page<StudentResponseDTO> getAllStudents(int page, int size) {
        log.info("Fetching all students, page: {}, size: {}", page, size);
        Page<StudentResponseDTO> students = repository.findAll(PageRequest.of(page, size))
                .map(mapper::toResponseDTO);
        log.info("Fetched {} students", students.getNumberOfElements());
        return students;
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student with ID: {}", id);
        Student student = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {}", id);
                    return new ResourceNotFoundException("Student not found with id: " + id);
                });
        log.info("Student found: {}", student.getName());
        return mapper.toDTO(student);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        log.info("Updating student with ID: {}", id);
        Student student = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {}", id);
                    return new ResourceNotFoundException("Student not found with id: " + id);
                });

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCourse(dto.getCourse());

        Student updated = repository.save(student);
        log.info("Student updated: {}", updated.getName());
        return mapper.toDTO(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        if (!repository.existsById(id)) {
            log.error("Student not found with ID: {}", id);
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        repository.deleteById(id);
        log.info("Student deleted with ID: {}", id);
    }
}
