package com.matthew.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    @Value("${proxy.host:localhost}")
    private String host;

    @Value("${proxy.port:80}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
