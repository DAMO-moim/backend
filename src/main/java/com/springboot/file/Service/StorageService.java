package com.springboot.file.Service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String fileName);
    //이후 s3로 확장할때 S3StorageService를 만들어서 저장
}