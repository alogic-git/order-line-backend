package com.orderline.basic.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.orderline.basic.model.dto.CommonDto;
import com.orderline.basic.service.AwsS3Service;
import com.orderline.basic.service.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags={"98.Basic"})
@RestController
@RequestMapping(path = "/basic", produces = MediaType.APPLICATION_JSON_VALUE)
public class BasicController {
    @Resource(name = "commonService")
    CommonService commonService;

    @Resource(name = "awsS3Service")
    AwsS3Service awsS;

    @ApiOperation(value = "email 발송", notes = "email을 발송합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "이메일 발송에 성공했습니다."),
    })
    @PostMapping("/sendEmail")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> sendEmail(
            @RequestBody @Valid CommonDto.EmailInfo emailInfo) {

        commonService.sendEmail(emailInfo.getTo(), emailInfo.getSubject(), emailInfo.getContents());

        return ResponseEntity.ok().build();
    }
}
