package com.nikita.testproject;


import com.nikita.testproject.fileUpDown.StorageProperties;
import com.nikita.testproject.config.OpenApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class, OpenApiConfig.class})
public class TestprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestprojectApplication.class, args);
    }

}
