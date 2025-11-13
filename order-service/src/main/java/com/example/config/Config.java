package com.example.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    @LoadBalanced // RẤT QUAN TRỌNG: Cho phép RestTemplate sử dụng tên service thay vì IP:Port
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}