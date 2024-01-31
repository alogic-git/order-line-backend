package com.ptglue.extra.controller;

import com.ptglue.extra.service.ExtraProductPurchaseService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = {"111. Extra Product Purchase"})
@RestController
@RequestMapping(path = {"extra-product-purchase"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ExtraProductPurchaseController {
    @Resource(name = "extraProductPurchaseService")
    ExtraProductPurchaseService extraProductPurchaseService;
}
