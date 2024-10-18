package com.clothes.service.impl;

import com.clothes.service.CloudinaryService;
import com.clothes.util.exception.BadRequestException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map<String, Object> uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            String publicId = (String) uploadResult.get("public_id");
            Map<String, Object> result = new HashMap<>();
            result.put("url", url);
            result.put("public_id", publicId);
            return result;
        } catch (Exception e) {
            throw new BadRequestException("Có lỗi xả ra trong quá trình lưu ảnh!");
        }
    }

    @Override
    public void deleteFileByUrl(String url) {
        try {
            String publicId = extractPublicIdFromUrl(url);
            deleteFile(publicId);
        } catch (Exception e) {
            throw new BadRequestException("Xoá ảnh không thành công!");
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new BadRequestException("Xoá ảnh không thành công!");
        }
    }

    private String extractPublicIdFromUrl(String url) {
        String[] parts = url.split("/");
        String publicIdWithFormat = parts[parts.length - 1];
        return publicIdWithFormat.substring(0, publicIdWithFormat.lastIndexOf('.'));
    }
}
