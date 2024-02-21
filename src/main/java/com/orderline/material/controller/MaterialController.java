package com.orderline.material.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"20.Material"})
@RestController
@RequestMapping(path = {"admin/material", "user/material"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class MaterialController {

}
