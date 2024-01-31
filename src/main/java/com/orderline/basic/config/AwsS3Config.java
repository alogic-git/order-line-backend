package com.orderline.basic.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

  private static final String AWS_S3_ACCESS_KEY = "AKIAQ3SPFWKTFI6KEK4F";
  private static final String AWS_S3_SECRET_KEY = "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";
  private static final String AWS_S3_REGION = "ap-northeast-2";
  @Bean
  public AmazonS3Client amazonS3Client() {
    BasicAWSCredentials awsCreeds = new BasicAWSCredentials(AWS_S3_ACCESS_KEY, AWS_S3_SECRET_KEY);
    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
        .withRegion(AWS_S3_REGION)
        .withCredentials(new AWSStaticCredentialsProvider(awsCreeds))
        .build();
  }
}
