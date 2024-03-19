package com.orderline.basic.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {
  
  @Value("${aws.s3.access-key}")
  private String AWS_S3_ACCESS_KEY;

  @Value("${aws.s3.secret-key}")
  private String AWS_S3_SECRET_KEY;

  @Value("${aws.s3.region}")
  private String AWS_S3_REGION;
  
  @Bean
  public AmazonS3Client amazonS3Client() {
    BasicAWSCredentials awsCreeds = new BasicAWSCredentials(AWS_S3_ACCESS_KEY, AWS_S3_SECRET_KEY);
    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
        .withRegion(AWS_S3_REGION)
        .withCredentials(new AWSStaticCredentialsProvider(awsCreeds))
        .build();
  }
}
