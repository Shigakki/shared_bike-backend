package com.example.bikes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BikesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikesApplication.class, args);
        System.out.println("访问Swagger地址:"+
                "http://localhost:9092/swagger-ui.html#/news-controller");
    }
}
