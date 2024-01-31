package com.ptglue.basic.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.basic.exception.ExternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

  private final AmazonS3Client amazonS3Client;
  private static final String AWS_S3_BUCKET_NAME = "ptglue-image";
  private static final String AWS_S3_CDN_NAME = "cdn-dev";


  public String upload(Long userId, MultipartFile file){
    if (file == null) throw new InternalServerErrorException("파일 업로드중 오류가 발생했습니다.[0]");
    String fileName = createFileName(userId, file.getOriginalFilename());
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());

    try(InputStream inputStream = file.getInputStream()) {
      amazonS3Client.putObject(new PutObjectRequest(AWS_S3_BUCKET_NAME, fileName, inputStream, objectMetadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));
    } catch(IOException e) {
      throw new ExternalServerErrorException("파일 업로드중 오류가 발생했습니다.[1]");
    }

    return getFullPath(fileName);
  }

  private String getFullPath(String fileName) {

    return "https://"+AWS_S3_CDN_NAME+".ptglue.com/" + fileName;
  }

  private String createFileName(Long userId, String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
    String extension = getFileExtension(fileName);
    String randomString = getRandomString()+extension;
    return userId.toString() + "/" + UUID.randomUUID().toString().concat(randomString);
  }

  private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    try {
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e) {
      throw new InternalServerErrorException("파일 업로드중 오류가 발생했습니다.[2]");
    }
  }

  public String getRandomString() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new SecureRandom();
    return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
  }
}
