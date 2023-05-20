package com.samnamja.deboost.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonFileConverter {
    public static void writeJsonToFile(Object data, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(fileName), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        writeJsonToFile(data, "output.json");

    }
}
