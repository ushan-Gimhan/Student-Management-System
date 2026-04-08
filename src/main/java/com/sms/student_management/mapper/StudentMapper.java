package com.sms.student_management.mapper;

import com.sms.student_management.dto.StudentDTO;
import com.sms.student_management.dto.StudentResponseDTO;
import com.sms.student_management.entity.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final ModelMapper modelMapper;

    public Student toEntity(StudentDTO dto) {
        return modelMapper.map(dto, Student.class);
    }

    public StudentDTO toDTO(Student entity) {
        return modelMapper.map(entity, StudentDTO.class);
    }
    public StudentResponseDTO toResponseDTO(Student entity) {
        return modelMapper.map(entity, StudentResponseDTO.class);
    }
}
