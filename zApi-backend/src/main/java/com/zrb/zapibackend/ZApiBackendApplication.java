package com.zrb.zapibackend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ZApiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZApiBackendApplication.class, args);
    }

}
