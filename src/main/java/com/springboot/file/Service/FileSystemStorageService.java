package com.springboot.file.Service;

import com.springboot.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Slf4j
public class FileSystemStorageService implements StorageService{
    private final Path rootLocation;
    private static final String[] ALLOWED_TYPES = {"jpg", "jpeg", "png", "gif"};

    public FileSystemStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(System.getProperty("user.dir"), uploadDir);
    }

    @Override
    public String store(MultipartFile file, String fileName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            // 확장자 확인 검증
            String originalFileName = file.getOriginalFilename();
            if(!isAllowedExtension(originalFileName)){
                throw new StorageException("File type not allowed: " + originalFileName);
            }

            String extent = getFileExtension(originalFileName);

            String newFileName = fileName + "." + extent;

            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(newFileName)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot upload file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                log.info("# store coffee image!!");
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return newFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to upload file.", e);
        }
    }
    private String getFileExtension(String fileName){
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if(lastIndexOfDot == -1){
            return ""; // 확장자 없을 때
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    private boolean isAllowedExtension(String fileName){
        String extension = getFileExtension(fileName);
        return Arrays.stream(ALLOWED_TYPES)
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }
}