package com.heekng.howtouses3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class AwsS3Config {

    public final String AWS_REGION = "ap-northeast-2";
    @Value("${aws.endpoint}")
    String AWS_ENDPOINT;

    public final String ACCESS_KEY = "test";
    public final String SECRET_KEY = "test";

    @Bean
    public AwsBasicCredentials credentials() {
        return AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return AwsCredentialsProviderChain.builder()
                .reuseLastProviderEnabled(true)
                .credentialsProviders(StaticCredentialsProvider.create(credentials()))
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(awsCredentialsProvider())
                .endpointOverride(URI.create(AWS_ENDPOINT))
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(awsCredentialsProvider())
                .endpointOverride(URI.create(AWS_ENDPOINT))
                .build();
    }

}
