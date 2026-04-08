package com.sms.student_management.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.student_management.dto.StudentDTO;
import com.sms.student_management.entity.Student;
import com.sms.student_management.exception.ResourceNotFoundException;
import com.sms.student_management.mapper.StudentMapper;
import com.sms.student_management.repository.StudentRepository;
import com.sms.student_management.service.SupabaseStorageService;
import com.sms.student_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    private final StudentMapper mapper;

    private final SupabaseStorageService storageService;

    @Override
    public StudentDTO saveStudent(String studentJson, MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            //Convert JSON → DTO
            StudentDTO dto = objectMapper.readValue(studentJson, StudentDTO.class);

            log.info("Saving new student: {}", dto.getName());

            //Upload file if exists
            if (file != null && !file.isEmpty()) {
                String imageUrl = storageService.uploadFile(file);
                dto.setProfileImageUrl(imageUrl);

                log.info("Image uploaded: {}", imageUrl);
            } else {
                log.warn("No image provided, saving without image");
            }

            //Save student
            Student student = mapper.toEntity(dto);
            Student saved = repository.save(student);

            log.info("Student saved with ID: {}", saved.getId());

            return mapper.toDTO(saved);

        } catch (Exception e) {
            log.error("Error saving student with image", e);
            throw new RuntimeException("Failed to save student", e);
        }
    }

    @Override
    public Page<StudentDTO> getAllStudents(int page, int size) {
        log.info("Fetching all students, page: {}, size: {}", page, size);
        Page<StudentDTO> students = repository.findAll(PageRequest.of(page, size))
                .map(mapper::toDTO);
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

    @Override
    public String updateProfileImage(Long id, MultipartFile file) {
        log.info("Update student Profile Image with ID: {}", id);
        //Check if the student exists in the database
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        // Delete old image (if exists)
        if (student.getProfileImageUrl() != null) {
            try {
                storageService.deleteFile(student.getProfileImageUrl());
            } catch (Exception e) {
                System.out.println("Old image delete failed, continuing...");
            }
        }

        //Upload new image
        String newImageUrl = storageService.uploadFile(file);

        //Update DB
        student.setProfileImageUrl(newImageUrl);
        repository.save(student);
        log.info("Student Profile Image Updated with ID: {}", id);

        return newImageUrl;
    }

    @Override
    public void deleteProfileImage(Long id) {
        log.info("Delete student Profile Image with ID: {}", id);
        //Check if the student exists in the database
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        //Check if student actually has an image
        String imageUrl = student.getProfileImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new ResourceNotFoundException("Student does not have a profile image to delete");
        }
        storageService.deleteFile(imageUrl);
        student.setProfileImageUrl(null);
        repository.save(student);
    }
}

