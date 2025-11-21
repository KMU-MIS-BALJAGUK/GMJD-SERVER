package org.baljaguk.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JWTConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expire-length}") // milliseconds
    private Long accessTokenExpiration;

    @Value("${jwt.header}")
    private String header; // e.g., "Authorization"
}
