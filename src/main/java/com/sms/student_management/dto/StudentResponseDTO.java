package com.sms.student_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String course;
    private String status; // "Active" or "Graduated"
}
