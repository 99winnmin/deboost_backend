package com.samnamja.deboost.utils.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samnamja.deboost.api.dto.openfeign.response.GameAllDetailInfoResponseDto;
import com.samnamja.deboost.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Uploader {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${file.path}")
    private String filePath;

    public String saveFileAndGetUrl(MultipartFile multipartFile) throws Exception {
        String s3FileName = filePath + "/" + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(multipartFile.getSize());

        amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, multipartFile.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    public String saveJsonStringAndGetKey(String jsonString, String summonerName) {
        String s3FileName = filePath + "/" + UUID.randomUUID() + "_" + summonerName + ".json";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType("application/json");
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        objMeta.setContentLength(bytes.length);

        InputStream inputStream = new ByteArrayInputStream(bytes);
        amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, inputStream, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3FileName; // return S3 Key
    }

    public GameAllDetailInfoResponseDto loadJsonFileAndConvertToDto(String s3Key) {
        ObjectMapper objectMapper = new ObjectMapper();
        S3Object s3Object = amazonS3.getObject(bucket, s3Key);
        S3ObjectInputStream s3is = s3Object.getObjectContent();
        try {
            String jsonString = new String(s3is.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonString, GameAllDetailInfoResponseDto.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message(e.getMessage()).build();
        }
    }
}
