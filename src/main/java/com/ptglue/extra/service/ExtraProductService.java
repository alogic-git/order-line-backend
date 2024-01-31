package com.ptglue.extra.service;

import com.ptglue.extra.repository.ExtraProductRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ExtraProductService {
    @Resource(name = "extraProductRepository")
    ExtraProductRepository extraProductRepository;
}
