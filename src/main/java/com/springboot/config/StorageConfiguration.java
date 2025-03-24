package com.springboot.config;

import com.springboot.file.Service.FileSystemStorageService;
import com.springboot.file.Service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class StorageConfiguration {
    @Bean
    public StorageService fileSystemStorageService(@Value("${file.upload-dir}") String uploadDir) {
        return new FileSystemStorageService(uploadDir);
    }
}