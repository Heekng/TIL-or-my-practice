package com.heekng.howtouses3.service;

import com.heekng.howtouses3.IntegrationTest;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.util.Map;

class S3FilePreSignedServiceTest extends IntegrationTest {

    @Autowired
    private S3FilePresignedService s3FilePresignedService;

    @Test
    @DisplayName("presigned 파일 업로드 테스트")
    void s3PutPresignedUrlTest() throws Exception {
        // pre signed url 발급
        var bucket = "file-bucket";
        var key = "sample-name.txt";

        String preSignedUrl = s3FilePresignedService.getPreSignedUrl(bucket, key, Map.of("meta1", "meta1-1"));

        Assertions.assertThat(preSignedUrl).isNotNull();

        // 파일 업로드
        var sampleFile = new ClassPathResource("static/sample.txt").getFile();

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            FileInputStream fileInputStream = new FileInputStream(sampleFile);

            HttpPut httpPut = new HttpPut(preSignedUrl);
            InputStreamEntity inputStreamEntity = new InputStreamEntity(
                    fileInputStream,
                    sampleFile.length(),
                    ContentType.TEXT_PLAIN
            );
            httpPut.setEntity(inputStreamEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                System.out.println("Response Code: " + response.getCode());
                System.out.println("Response Message: " + response.getReasonPhrase());
            }
        }
    }
}