package com.mopl.storage.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;

@ConfigurationProperties("mopl.storage.local")
@Validated
public record LocalStorageProperties(
    @NotNull Path rootPath,
    @NotNull String baseUrl
) {
}
