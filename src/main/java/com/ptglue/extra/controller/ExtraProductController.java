package com.ptglue.extra.controller;

import com.ptglue.extra.service.ExtraProductService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = {"110. Extra Product"})
@RestController
@RequestMapping(path = {"extra-product"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ExtraProductController {
    @Resource(name = "extraProductService")
    ExtraProductService extraProductService;
}
