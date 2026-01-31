package com.postechfiap.msoperacional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableCaching
@SpringBootApplication
public class MsOperacionalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsOperacionalApplication.class, args);
    }

}
