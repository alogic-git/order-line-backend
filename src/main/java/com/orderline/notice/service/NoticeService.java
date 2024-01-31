package com.orderline.notice.service;

import com.orderline.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NoticeService {

    @Resource(name = "noticeRepository")
    NoticeRepository noticeRepository;
}
