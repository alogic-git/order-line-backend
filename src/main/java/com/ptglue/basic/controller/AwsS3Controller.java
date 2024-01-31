package com.ptglue.basic.controller;

import io.swagger.annotations.*;
import com.ptglue.basic.model.dto.CreateResponseDto;
import com.ptglue.basic.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

@Api(tags = {"00.Aws S3"})
@RestController
@RequestMapping(path = "/s3", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AwsS3Controller {

    @Resource(name = "awsS3Service")
    AwsS3Service awsS3Service;

    @ApiOperation(value = "aws s3 파일 업로드")
    @ApiResponses({
            @ApiResponse(code=400, message="존재하지 않는 유저입니다.")
    })
    @PostMapping("upload")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<CreateResponseDto.S3UploadResponseDto> create(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "파일(MultipartFile)", required = true) @RequestPart("file") MultipartFile file) throws IOException {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        String resultUri = awsS3Service.upload(userId, file);

        return ResponseEntity.created(URI.create(resultUri)).body(CreateResponseDto.S3UploadResponseDto.builder().imageUri(resultUri).build());
    }

}
