package com.mopl.storage.provider;

import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface FileStorageProvider {

    String upload(InputStream inputStream, String path);

    String getUrl(String path);

    Resource load(String relativePath);

    void delete(String path);
}
