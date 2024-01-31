package com.ptglue.extra.service;

import com.ptglue.extra.repository.ExtraProductPurchaseRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ExtraProductPurchaseService {
    @Resource(name = "extraProductPurchaseRepository")
    ExtraProductPurchaseRepository extraProductPurchaseRepository;

}
