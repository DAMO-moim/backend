package com.springboot.file.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class S3StorageService implements StorageService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3StorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String store(MultipartFile file, String fileNameWithoutExt) {
        try {
            String extension = getExtension(file.getOriginalFilename());
            String fileName = fileNameWithoutExt + "." + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata);
            amazonS3.putObject(putObjectRequest);
            log.info("S3 업로드 완료: {}", fileName);

            return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName; // 또는 필요 시 전체 URL 반환 가능
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 실패", e);
        }
    }

    @Override
    public void delete(String relativePath) {
        amazonS3.deleteObject(bucket, relativePath);
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex > -1 ? fileName.substring(dotIndex + 1).toLowerCase() : "";
    }
}
