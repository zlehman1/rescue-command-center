package org.rescue.command.center.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BaseApplication.class, args);
    }
}