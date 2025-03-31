package com.springboot.file.Service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String fileName);
    //용량이 한정적이므로 수정 전 이미지는 삭제한다.

    void delete(String relativePath);
    //이후 s3로 확장할때 S3StorageService를 만들어서 저장
}