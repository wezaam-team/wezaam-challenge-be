package com.wezaam.withdrawal.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    public static final String API_VERSION = "1.0.0";
    public static final String API_TITLE = "wezaam_challenge_withdrawal_v1";
    private static final String API_DESCRIPTION = "API Withdrawal";

    @Bean
    public Docket api(ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .build();
    }

}
