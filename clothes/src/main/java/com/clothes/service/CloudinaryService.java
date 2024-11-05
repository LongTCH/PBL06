package com.clothes.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map<String, Object> uploadFile(MultipartFile file);

    void deleteFileByUrl(String url);

    void deleteFile(String publicId);
}
