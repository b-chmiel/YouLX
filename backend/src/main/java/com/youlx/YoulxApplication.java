package com.youlx;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "X-API-KEY", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
public class YoulxApplication {
    public static void main(String[] args) {
        SpringApplication.run(YoulxApplication.class, args);
    }
}
