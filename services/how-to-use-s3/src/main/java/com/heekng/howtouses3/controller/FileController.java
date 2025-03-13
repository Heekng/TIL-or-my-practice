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
        return s3FileMemoryService.upload("file-service-test-hee", key, file.getBytes());
    }

    @GetMapping("/presigned/url")
    public String presignedUrl(@RequestParam String contentType, @RequestParam Long contentLength) {
        String key = UUID.randomUUID().toString().substring(0, 10);
        return s3FilePresignedService.getPreSignedUrl("file-service-test-hee", key, Map.of("aaa", "aaaa", "bbb", "bbbb"), contentType, contentLength);
        // return s3FilePresignedService.getPreSignedUrl("file-service-test-hee", key, Map.of("Cache-Control", "no-cache", "Content-Type", "application/json"));
    }

}
