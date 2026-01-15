package com.mopl.external.config;

import com.mopl.external.properteis.TmdbProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(TmdbProperties.class)
public class WebClientConfig {

    @Bean
    public WebClient tmdbWebClient(TmdbProperties props) {
        return WebClient.builder()
            .baseUrl(props.getApi().getBaseUrl())
            .defaultHeader(
                "Authorization",
                "Bearer " + props.getApi().getAccessToken()
            )
            .build();
    }

    @Bean
    public WebClient tmdbImageClient(TmdbProperties props) {
        return WebClient.builder()
            .baseUrl(props.getImage().getBaseUrl())
            .build();
    }
}
