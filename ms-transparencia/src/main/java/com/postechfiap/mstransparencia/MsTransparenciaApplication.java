package com.postechfiap.mstransparencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTransparenciaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTransparenciaApplication.class, args);
    }

}
