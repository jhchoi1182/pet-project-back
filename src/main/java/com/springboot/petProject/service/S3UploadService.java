package com.springboot.petProject.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadBase64Images(List<String> base64Images) {
        List<String> imageUrls = new ArrayList<>();

        for (String base64Image : base64Images) {
            // 파일명 생성
            String fileName = UUID.randomUUID().toString() + ".png";

            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(imageBytes.length);
                metadata.setContentType("image/png");

                amazonS3.putObject(bucketName, fileName, inputStream, metadata);

                String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
                imageUrls.add(fileUrl);
            } catch (Exception e) {
                throw new CustomExceptionHandler(ErrorCode.S3_ERROR);
            }
        }

        return imageUrls;
    }
}