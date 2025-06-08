package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan("com.example")
@MapperScan("com.example.mapper")
@SpringBootApplication
public class MinIOApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinIOApplication.class, args);
    }
}