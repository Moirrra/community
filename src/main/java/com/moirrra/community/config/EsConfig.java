package com.moirrra.community.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-30
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class EsConfig {
    @Value("${spring.elasticsearch.uris}")
    private String esUrl;

    @Bean
    RestHighLevelClient client() {
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(esUrl)
                .build();
        return RestClients.create(configuration).rest();
    }
}
