package com.sms.student_management.service;

import org.springframework.web.multipart.MultipartFile;

public interface SupabaseStorageService {
    //upload file to supabase
    String uploadFile(MultipartFile file);
    //delete file in supabase
    void deleteFile(String imageUrl);
}
