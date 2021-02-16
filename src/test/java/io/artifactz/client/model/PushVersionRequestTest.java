package io.artifactz.client.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PushVersionRequestTest extends JSONTest {
    @Test
    public void testMarshalling() throws Exception {
        PushVersionRequest request = new PushVersionRequest();
        request.setStage("Development");
        request.setName("test-data");
        request.setVersion("1.0.0");

        String jsonString = objectMapper.writeValueAsString(request);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertEquals("Development", ctx.read("$.stage_name"));
        assertEquals("test-data", ctx.read("$.artifact_name"));
        assertEquals("1.0.0", ctx.read("$.version"));
    }

    @Test
    public void testMarshallingEmpty() throws Exception {
        PushVersionRequest request = new PushVersionRequest();

        String jsonString = objectMapper.writeValueAsString(request);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertTrue(assertDoesNotExist(ctx, "$.stage_name"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_name"));
        assertTrue(assertDoesNotExist(ctx, "$.version"));
    }

    @Test
    public void testUnmarshalling() throws Exception {
        String payload = "{ " +
                "  \"stage_name\": \"Development\", " +
                "  \"artifact_name\": \"test-data\", " +
                "  \"version\": \"1.0.0\" " +
                "}";

        PushVersionRequest request = objectMapper.readValue(payload, PushVersionRequest.class);
        assertEquals("Development", request.getStage());
        assertEquals("test-data", request.getName());
        assertEquals("1.0.0", request.getVersion());
    }

    @Test
    public void testUnmarshallingEmpty() throws Exception {
        String payload = "{ }";

        try {
            objectMapper.readValue(payload, PublishVersionRequest.class);
            fail();
        } catch (Exception e) {

        }
    }
}
