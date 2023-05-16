package com.common.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: swh 20301169@bjtu.edu.cn
 * @description:
 */

@Configuration
@EnableSwagger2
//prefix+name通过application.yml文件配置是否启动swagger在线生成文档
public class SwaggerConfig {
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 基本信息的配置，信息会在 Api 文档上显示
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("仓库管理系统文档")
                .description("仓库管理系统所有子系统相关的接口文档")
                .termsOfServiceUrl("http://localhost:8084")
                .version("1.0")
                .build();
    }
}
