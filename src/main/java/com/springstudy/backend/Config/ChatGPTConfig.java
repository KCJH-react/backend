package com.springstudy.backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGPTConfig {
    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.model}")
    private String model;

    public String getSecretKey() {
        return secretKey;
    }

    public String getModel() {
        return model;
    }
}
