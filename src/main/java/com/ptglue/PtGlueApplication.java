
package com.ptglue;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"99.PtGlueApplication"})
@SpringBootApplication
@RestController
public class PtGlueApplication {

    public static void main(String[] args) {
        SpringApplication.run(PtGlueApplication.class, args);
    }

    @ApiOperation(value = "PtGlueApplication")
    @GetMapping("/")
    public String status() {
        return "PtGlueApplication Server is On";
    }
}
