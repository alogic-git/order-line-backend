
package com.orderline;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"99.OrderlineApplication"})
@SpringBootApplication
@RestController
public class OrderLineApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderLineApplication.class, args);
    }

    @ApiOperation(value = "OrderlineApplication")
    @GetMapping("/")
    public String status() {
        return "OrderlineApplication Server is On";
    }
}
