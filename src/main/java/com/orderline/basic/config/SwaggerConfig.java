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
            "sample user token: username = gobaebae, name = 고배배\n" +
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJzaXRlSWQiOjEsImlhdCI6MTcxMDQ4OTcxNywiZXhwIjoxNzE4MjY1NzE3fQ.sMBmTejV3VtnxO8zVU9MmEDywfOxyQ0XOwlgby3zcdM\n" +
            "sample admin token: username = admin, name = 관리자\n" +
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IkFETUlOIiwic2l0ZUlkIjo1LCJpYXQiOjE3MTA0OTAyNDIsImV4cCI6MTcxODI2NjI0Mn0.jt1sKEI2Ia5BDG0XGVzgd6iCchOLxTkFa9WjwHvOGA8";
  private static final String PACKAGE_NAME = "com.orderline";

  @Bean
  public Docket commonApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("common")
            .apiInfo(apiInfo())
            .securityContexts(Collections.singletonList(securityContext()))
            .securitySchemes(Collections.singletonList(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage(PACKAGE_NAME))
            .paths(PathSelectors.ant("/common/**"))
            .build();
  }
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
            .paths(PathSelectors.ant("/admin/**").or(not(PathSelectors.ant("/common/**").or((PathSelectors.ant("/user/**"))))))
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
