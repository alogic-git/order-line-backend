package com.orderline.site.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"30.Site"})
@RestController
@RequestMapping(path = {"admin/site", "user/site"}, produces = MediaType.APPLICATION_JSON_VALUE)

public class SiteController {
}
