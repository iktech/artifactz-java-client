package io.artifactz.client.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class VersionTest extends JSONTest {
    @Test
    public void testMarshallingEmpty() throws Exception {
        Version request = new Version();

        String jsonString = objectMapper.writeValueAsString(request);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertTrue(assertDoesNotExist(ctx, "$.artifact_name"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_description"));
        assertTrue(assertDoesNotExist(ctx, "$.type"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_id"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_name"));
        assertTrue(assertDoesNotExist(ctx, "$.version"));
    }

    @Test
    public void testUnmarshallingEmpty() throws Exception {
        String payload = "{ }";

        try {
            objectMapper.readValue(payload, Version.class);
            fail();
        } catch (Exception e) {

        }
    }
}
