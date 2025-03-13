package com.heekng.howtouses3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3FileMemoryService {

    private final S3Client s3Client;

    public String upload(String bucket, String key, byte[] file) {
        s3Client.putObject(builder -> {
                    builder.bucket(bucket);
                    builder.key(key);
                },
                RequestBody.fromBytes(file));

        S3Utilities s3Utilities = s3Client.utilities();
        URL url = s3Utilities.getUrl(builder -> {
            builder.bucket(bucket);
            builder.key(key);
        });
        return url.toString();
    }

    public byte[] get(String bucket, String key) {
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(builder -> builder.bucket(bucket).key(key));
        try {
            return response.readAllBytes();
        } catch (IOException e) {
        }
        return null;
    }
}
