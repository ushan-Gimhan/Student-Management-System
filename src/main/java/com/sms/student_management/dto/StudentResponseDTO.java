package com.sms.student_management.dto;

import lombok.Data;

@Data
public class StudentResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String course;
    private String status; // "Active" or "Graduated"
}
