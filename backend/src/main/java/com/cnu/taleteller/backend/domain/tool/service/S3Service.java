package com.cnu.taleteller.backend.domain.tool.service;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.cnu.taleteller.backend.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public Map<String, Serializable> getPreSignedUrl(String fileName) {
        String encodedFileName = LocalDateTime.now() + "_" + fileName;
        String objectKey = "static/" + encodedFileName;

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += (3 * 60 * 1000); // 3 minutes
        expiration.setTime(expTimeMillis); // Set URL expiration time

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        Map<String, Serializable> result = new HashMap<>();
        result.put("preSignedUrl", s3Config.amazonS3Client().generatePresignedUrl(generatePresignedUrlRequest));
        result.put("encodedFileName", encodedFileName);

        return result;
    }
    public void uploadFile(Serializable fileName, InputStream inputStream) {
        String objectKey = "static/" + fileName;

        s3Config.amazonS3Client().putObject(bucket, objectKey, inputStream, null);
    }
}