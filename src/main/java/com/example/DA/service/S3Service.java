package com.example.DA.service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 s3Client; // Khai báo s3Client làm biến instance

    @Value("${aws.s3.bucket}") // Sử dụng annotation đúng cho bucketName
    private String bucketName;

    // Constructor khởi tạo với các giá trị từ application.properties
    public S3Service(@Value("${aws.accessKeyId}") String accessKey,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.region}") String region) {
        System.out.println("S3");
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
        for (Bucket bucket : s3Client.listBuckets()) {
            System.out.println("* " + bucket.getName());
        }
    }

    // Phương thức upload file lên S3
    public String uploadFile(String folderName, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        String s3FilePath = folderName + "/" + fileName; // Ví dụ: "propertyId/filename.jpg"
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        s3Client.putObject(bucketName, s3FilePath, file.getInputStream(), metadata);
        return s3Client.getUrl(bucketName, s3FilePath).toString();
    }
}
