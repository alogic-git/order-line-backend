package com.orderline.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Profile(value = {"local", "dev", "prd"})
@Configuration
//@EnableSwagger2
public class SwaggerConfig {
  private static final String TITLE = "ORDERLINE";
  private static final String VERSION = "0.0.1";
  private static final String DESCRIPTION = "orderline api\n" +
            "sample user token: username = gobaebae3, name = 고배배3\n" +
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1IiwiaWF0IjoxNzA4NjU0Mjg5LCJleHAiOjE3MTY0MzAyODl9.T9-wIj2VTIYfOlDsyjuapQBrHkxI-sXGYRFcA1ADLLs";
//          "sample tutee(상현2825) id: 266 \n" +
//          "token: \n " +
//          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNjYiLCJyb2xlIjoiVFVURUUiLCJicmFuY2hJZCI6OTgsImlhdCI6MTcwMDE4ODY2MiwiZXhwIjoxNzA3OTY0NjYyfQ.qYFFbsg2eaRWquNcbkUqaf4pNOZrmETf9ESrRDq6Iy4";
  private static final String PACKAGE_NAME = "com.orderline";

  @Bean
  public Docket userApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("user")
            .apiInfo(apiInfo())
            .securityContexts(Collections.singletonList(securityContext()))
            .securitySchemes(Collections.singletonList(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage(PACKAGE_NAME))
            .paths(PathSelectors.ant("/user/**"))
            .build();
  }

  @Bean
  public Docket adminApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("admin")
            .apiInfo(apiInfo())
            .securityContexts(Collections.singletonList(securityContext()))
            .securitySchemes(Collections.singletonList(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage(PACKAGE_NAME))
            .paths(PathSelectors.ant("/admin/**").or(not(PathSelectors.ant("/user/**").or(PathSelectors.ant("/auth/**")))))
            .build();
  }

    private static Predicate<String> not(Predicate<String> target) {
        return target.negate();
    }

  private ApiKey apiKey() {
    return new ApiKey("JWT", "X-AUTH-TOKEN", "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth()).build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
  }

  public ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title(TITLE)
            .version(VERSION)
            .description(DESCRIPTION)
            .build();
  }
}
