package com.lseraponte.cupidapi.hh.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    // Method to read JSON from a file and map it to a single object
    public static <T> T readJsonFromFile(String filePath, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new File(filePath), clazz);
    }

    // Method to read JSON from a file and map it to a list of objects
    public static <T> List<T> readJsonListFromFile(String filePath, Class<T> clazz) throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.readValue(new File(filePath), type);
    }
}
