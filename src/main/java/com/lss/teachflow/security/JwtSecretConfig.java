package com.lss.teachflow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Base64;

@Configuration
public class JwtSecretConfig {
    @Bean
    public String jwtSecret() {
        byte[] key = new byte[32]; // 256位
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
