package com.orderline.notice.controller;

import com.orderline.notice.service.NoticeService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = {"70.Notice"})
@RestController
@RequestMapping(path = "notice", produces = MediaType.APPLICATION_JSON_VALUE)
public class NoticeController {
    @Resource(name = "noticeService")
    NoticeService noticeService;
}
