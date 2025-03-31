package com.springboot.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.springboot.file.Service.FileSystemStorageService;
import com.springboot.file.Service.S3StorageService;
import com.springboot.file.Service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class StorageConfiguration {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    @Primary  // 우선순위 부여: S3StorageService가 기본 StorageService가 됨
    @Bean
    public StorageService s3StorageService(AmazonS3 amazonS3) {
        return new S3StorageService(amazonS3);
    }

    @Bean
    public StorageService fileSystemStorageService(@Value("${file.upload-dir}") String uploadDir) {
        return new FileSystemStorageService(uploadDir);
    }
}