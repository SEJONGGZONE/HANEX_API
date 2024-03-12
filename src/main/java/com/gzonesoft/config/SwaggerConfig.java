package com.gzonesoft.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration("swaggerConfig")
@EnableSwagger2
public class SwaggerConfig {

	//http://localhost:8080/swagger-ui.html

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //.apis(RequestHandlerSelectors.basePackage("example.api.controll"))
                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.ant("/api/**")) // 그중 /api/** 인 URL들만 필터링
                //.paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalResponseMessage(RequestMethod.POST,
                        Arrays.asList(
                                new ResponseMessageBuilder()
                                        .code(1000)
                                        .message("Success Return Results")
                                        .build(),
                                        new ResponseMessageBuilder()
                                        .code(1001)
                                        .message("Unexpected error")
                                        .build()                                        
                        )
                )
                ;

    }
    
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
         "HANEX-TMS 구축프로젝트 - 2024",
         "Mobile API",
          "API ver 1.0-0312",
          "Terms of service", 
          "", "","");
   }
}
