package com.zrb.zapisdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "zapi")
// 将配置文件中的配置映射到类中
// prefix=zApi会报错
@Data
@ComponentScan
public class ZApiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public ZApiClient zApiClient(){
        return new ZApiClient(accessKey,secretKey);
    }
}
