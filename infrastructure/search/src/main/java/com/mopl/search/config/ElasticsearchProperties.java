package com.mopl.search.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mopl.search.elasticsearch")
public class ElasticsearchProperties {

    private String uris;

    private String username;
    private String password;

    private Duration connectTimeout = Duration.ofSeconds(1);
    private Duration socketTimeout = Duration.ofSeconds(3);
}
