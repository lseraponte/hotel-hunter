package com.lseraponte.cupidapi.hh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://content-api.cupid.travel/v3.0")
                .defaultHeader("x-api-key", "i2O4p6A8s0D3f5G7h9J1k3L5m7N9b")
                .build();
    }

}
