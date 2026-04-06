package com.sms.student_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.apiKey}")
    private String supabaseApiKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    public String getSupabaseUrl(){
        return supabaseUrl;
    }
    public String getSupabaseApiKey(){
        return supabaseApiKey;
    }
    public String getSupabaseBucket(){
        return supabaseBucket;
    }

}
