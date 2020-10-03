package cc.holdinga.covidtracker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parse(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, valueType);
    }

    public static String stringify(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

}
