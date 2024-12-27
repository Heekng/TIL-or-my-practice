package com.heekng.howtouses3.controller;

import com.heekng.howtouses3.service.S3FileMemoryService;
import com.heekng.howtouses3.service.S3FilePresignedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final S3FileMemoryService s3FileMemoryService;
    private final S3FilePresignedService s3FilePresignedService;

    @PostMapping("/memory/upload")
    public String memoryUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString().substring(0, 10);
        return s3FileMemoryService.upload("test-bucket", key, file.getBytes());
    }

    @GetMapping("/presigned/url")
    public String presignedUrl() {
        String key = UUID.randomUUID().toString().substring(0, 10);
        return s3FilePresignedService.getPreSignedUrl("test-bucket", key, Map.of());
    }

}
