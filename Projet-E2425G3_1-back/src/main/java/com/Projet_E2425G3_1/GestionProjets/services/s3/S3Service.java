package com.Projet_E2425G3_1.GestionProjets.services.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = "user-profile-images/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(uniqueFileName)
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region,
                uniqueFileName);
    }
    public boolean isSupportedImageFormat(String contentType) {
        return contentType.equals("image/jpeg") ||contentType.equals("application/octet-stream") || contentType.equals("image/png") || contentType.equals("image/jpg");
    }


}
