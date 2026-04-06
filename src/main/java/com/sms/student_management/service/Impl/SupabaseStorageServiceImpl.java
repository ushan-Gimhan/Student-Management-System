package com.sms.student_management.service.Impl;

import com.sms.student_management.config.SupabaseConfig;
import com.sms.student_management.service.SupabaseStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseStorageServiceImpl implements SupabaseStorageService {

    private final SupabaseConfig supabaseConfig;

    private final WebClient webClient = WebClient.builder().build();

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadUrl = String.format("%s/storage/v1/object/%s/%s",
                supabaseConfig.getSupabaseUrl(),
                supabaseConfig.getSupabaseBucket(),
                fileName);

        try {
            webClient.post()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + supabaseConfig.getSupabaseApiKey())
                    .header("apiKey", supabaseConfig.getSupabaseApiKey())
                    .header("Content-Type", file.getContentType())
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class).flatMap(errorBody -> {
                                // THIS LINE IS THE KEY: It prints the real error to your IDE console
                                System.err.println("Supabase Error Response: " + errorBody);
                                return Mono.error(new RuntimeException("Supabase Error: " + errorBody));
                            })
                    )
                    .toBodilessEntity()
                    .block();

            return String.format("%s/storage/v1/object/public/%s/%s",
                    supabaseConfig.getSupabaseUrl(),
                    supabaseConfig.getSupabaseBucket(),
                    fileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String imageUrl) {
        // Extract filename from URL (The part after the last '/')
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        String deleteUrl = String.format("%s/storage/v1/object/%s/%s",
                supabaseConfig.getSupabaseUrl(), supabaseConfig.getSupabaseBucket(), fileName);

        webClient.delete()
                .uri(deleteUrl)
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseApiKey())
                .header("apiKey", supabaseConfig.getSupabaseApiKey())
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class).flatMap(error -> {
                            log.error("Supabase Delete Error: {}", error);
                            return Mono.error(new RuntimeException("Delete failed: " + error));
                        })
                )
                .toBodilessEntity()
                .block();
    }
}
