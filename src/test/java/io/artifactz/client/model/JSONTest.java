package io.artifactz.client.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;

public class JSONTest {
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected boolean assertDoesNotExist(DocumentContext ctx, String jsonPath) {
        try {
            ctx.read(jsonPath);
            return false;
        } catch(Exception e) {
            return true;
        }
    }
}
