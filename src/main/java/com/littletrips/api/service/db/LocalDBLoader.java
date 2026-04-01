package com.littletrips.api.service.db;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class LocalDBLoader {
    private final ResourceLoader resourceLoader;

    public LocalDBLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public <T> List<T> load(String path, Class<T> clazz) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + path);
            InputStream inputStream = resource.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
