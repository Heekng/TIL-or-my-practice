package com.heekng.howtouses3.service;

import com.heekng.howtouses3.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

class S3FileMemoryServiceTest extends IntegrationTest {

    @Autowired
    private S3FileMemoryService s3FileMemoryService;

    @Test
    @DisplayName("파일 업로드 테스트")
    void s3PutTest() throws Exception {
        // given
        var bucket = "file-bucket";
        var key = "sample-name.txt";
        var sampleFile = new ClassPathResource("static/sample.txt").getFile();

        // when
        String upload = s3FileMemoryService.upload(bucket, key, sampleFile);
        System.out.println(upload);

        // then
        byte[] bytes = s3FileMemoryService.get(bucket, key);

        // expected
        assertThat(bytes).isNotNull();
    }


}