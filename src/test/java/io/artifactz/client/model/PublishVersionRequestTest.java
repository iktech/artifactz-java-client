package io.artifactz.client.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublishVersionRequestTest extends JSONTest {
    @Test
    public void testMarshalling() throws Exception {
        PublishVersionRequest request = new PublishVersionRequest();
        request.setStage("Development");
        request.setStageDescription("Artifacts Development");
        request.setName("test-data");
        request.setDescription("Test data library");
        request.setFlow("Standard");
        request.setType("JAR");
        request.setGroupId("io.iktech.test");
        request.setArtifactId("test-data");
        request.setVersion("1.0.0");

        String jsonString = objectMapper.writeValueAsString(request);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertEquals("Development", ctx.read("$.stage"));
        assertEquals("Artifacts Development", ctx.read("$.stage_description"));
        assertEquals("test-data", ctx.read("$.artifact_name"));
        assertEquals("Test data library", ctx.read("$.artifact_description"));
        assertEquals("Standard", ctx.read("$.flow"));
        assertEquals("JAR", ctx.read("$.type"));
        assertEquals("io.iktech.test", ctx.read("$.group_id"));
        assertEquals("test-data", ctx.read("$.artifact_id"));
        assertEquals("1.0.0", ctx.read("$.version"));
    }

    @Test
    public void testMarshallingEmpty() throws Exception {
        PublishVersionRequest request = new PublishVersionRequest();

        String jsonString = objectMapper.writeValueAsString(request);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertTrue(assertDoesNotExist(ctx, "$.stage"));
        assertTrue(assertDoesNotExist(ctx, "$.stage_description"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_name"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_description"));
        assertTrue(assertDoesNotExist(ctx, "$.flow"));
        assertTrue(assertDoesNotExist(ctx, "$.type"));
        assertTrue(assertDoesNotExist(ctx, "$.group_id"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_id"));
        assertTrue(assertDoesNotExist(ctx, "$.version"));
    }

    @Test
    public void testUnmarshalling() throws Exception {
        String payload = "{ " +
                "  \"stage\": \"Development\", " +
                "  \"stage_description\": \"Artifacts Development\", " +
                "  \"artifact_name\": \"test-data\", " +
                "  \"artifact_description\": \"Test data library\", " +
                "  \"flow\": \"Standard\", " +
                "  \"type\": \"JAR\", " +
                "  \"group_id\": \"io.iktech.test\", " +
                "  \"artifact_id\": \"test-data\", " +
                "  \"version\": \"1.0.0\" " +
                "}";

        PublishVersionRequest request = objectMapper.readValue(payload, PublishVersionRequest.class);
        assertEquals("Development", request.getStage());
        assertEquals("Artifacts Development", request.getStageDescription());
        assertEquals("test-data", request.getName());
        assertEquals("Test data library", request.getDescription());
        assertEquals("Standard", request.getFlow());
        assertEquals("JAR", request.getType());
        assertEquals("io.iktech.test", request.getGroupId());
        assertEquals("test-data", request.getArtifactId());
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
