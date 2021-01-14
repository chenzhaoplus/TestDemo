package com.example.demo;

import com.smcaiot.cloud.common.annotation.EnableSwagger;
import com.smcaiot.cloud.common.annotation.EnableWebLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients
@EnableWebLog
@EnableSwagger
public class FlinkExecutorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlinkExecutorDemoApplication.class, args);
    }

}
