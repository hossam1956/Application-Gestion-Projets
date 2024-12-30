package com.Projet_E2425G3_1.GestionProjets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BucketConfig {

    @Value("${aws.access.key}")
    String awsAccessKey;

    @Value("${aws.secret.key}")
    String awsSecretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public S3Client getAmazonS3Client() {
        AwsBasicCredentials awsCredentails = AwsBasicCredentials.create(awsAccessKey,awsSecretKey) ;
        return  S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentails))
                .build();
    }
}
