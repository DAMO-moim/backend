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
    public String store(MultipartFile file, String fileNameWithoutExt) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            // 확장자 확인 검증
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(originalFileName);

            if(!isAllowedExtension(extension)){
                throw new StorageException("File type not allowed: " + extension);
            }

            String fullRelativePath = fileNameWithoutExt + "." + extension;
            Path destinationFile = this.rootLocation.resolve(fullRelativePath)
                    .normalize().toAbsolutePath();
//            Path destinationFile = this.rootLocation.resolve(
//                    Paths.get(newFileName)).normalize().toAbsolutePath();

            //equals는 서로 다르다고 판단하므로 startWith() 사용
            if (!destinationFile.startsWith(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot upload file outside current directory");
            }
            Files.createDirectories(destinationFile.getParent());
            try (InputStream inputStream = file.getInputStream()) {
                log.info("# store coffee image!!");
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return fullRelativePath;
        } catch (IOException e) {
            throw new StorageException("Failed to upload file.", e);
        }
    }

    private String getFileExtension(String fileName){
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot == -1) return "";
        return fileName.substring(lastDot + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String extension) {
        return Arrays.stream(ALLOWED_TYPES)
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }

    @Override
    public void delete(String relativePath) {
        try {
            Path filePath = this.rootLocation.resolve(relativePath).normalize().toAbsolutePath();

            // 보안 체크 -> 루트 디렉토리 내에 있는지 확인한다.
            if (!filePath.startsWith(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot delete file outside current directory");
            }

            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file", e);
        }
    }
}