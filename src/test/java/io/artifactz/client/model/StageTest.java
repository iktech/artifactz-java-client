package io.artifactz.client.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StageTest extends JSONTest {
    @Test
    public void testMarshalling() throws Exception {
        Stage request = new Stage();
        request.setStage("Development");
        request.setArtifacts(new ArrayList<>());

        Version version = new Version();
        version.setArtifactName("test-data");
        version.setArtifactDescription("Test Data Library");
        version.setType("JAR");
        version.setGroupId("io.iktech.test");
        version.setArtifactId("test-data");
        version.setVersion("1.0.0");

        request.getArtifacts().add(version);

        version = new Version();
        version.setArtifactName("test-api");
        version.setArtifactDescription("Test API");
        version.setType("DockerImage");
        version.setVersion("1.0.1");
        request.getArtifacts().add(version);

        String jsonString = objectMapper.writeValueAsString(request);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertEquals("Development", ctx.read("$.stage"));
        assertEquals(2, (Integer)ctx.read("$.artifacts.length()"));
        assertEquals("test-data", ctx.read("$.artifacts[0].artifact_name"));
        assertEquals("Test Data Library", ctx.read("$.artifacts[0].artifact_description"));
        assertEquals("JAR", ctx.read("$.artifacts[0].type"));
        assertEquals("io.iktech.test", ctx.read("$.artifacts[0].group_id"));
        assertEquals("test-data", ctx.read("$.artifacts[0].artifact_id"));
        assertEquals("1.0.0", ctx.read("$.artifacts[0].version"));

        assertEquals("test-api", ctx.read("$.artifacts[1].artifact_name"));
        assertEquals("Test API", ctx.read("$.artifacts[1].artifact_description"));
        assertEquals("DockerImage", ctx.read("$.artifacts[1].type"));
        assertTrue(assertDoesNotExist(ctx, "$.artifacts[1].artifact_id"));
        assertTrue(assertDoesNotExist(ctx, "$.artifact_name"));
        assertEquals("1.0.1", ctx.read("$.artifacts[1].version"));
    }

    @Test
    public void testMarshallingEmpty() throws Exception {
        Stage request = new Stage();

        String jsonString = objectMapper.writeValueAsString(request);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertTrue(assertDoesNotExist(ctx, "$.stage"));
        assertTrue(assertDoesNotExist(ctx, "$.artifacts"));
    }

    @Test
    public void testUnmarshalling() throws Exception {
        String payload = "{ " +
                "  \"stage\": \"Development\", " +
                "  \"artifacts\": [" +
                " {" +
                "  \"artifact_name\": \"test-data\", " +
                "  \"artifact_description\": \"Test Data Library\", " +
                "  \"type\": \"JAR\", " +
                "  \"group_id\": \"io.iktech.test\", " +
                "  \"artifact_id\": \"test-data\", " +
                "  \"version\": \"1.0.0\" " +
                " }, {" +
                "  \"artifact_name\": \"test-api\", " +
                "  \"artifact_description\": \"Test API\", " +
                "  \"type\": \"DockerImage\", " +
                "  \"version\": \"1.0.1\" " +
                "}]}";

        Stage request = objectMapper.readValue(payload, Stage.class);
        assertEquals("Development", request.getStage());
        assertNotNull(request.getArtifacts());
        assertEquals(2, request.getArtifacts().size());
        Version version = request.getArtifacts().get(0);
        assertEquals("test-data", version.getArtifactName());
        assertEquals("Test Data Library", version.getArtifactDescription());
        assertEquals("JAR", version.getType());
        assertEquals("io.iktech.test", version.getGroupId());
        assertEquals("test-data", version.getArtifactId());
        assertEquals("1.0.0", version.getVersion());
        version = request.getArtifacts().get(1);
        assertEquals("test-api", version.getArtifactName());
        assertEquals("Test API", version.getArtifactDescription());
        assertEquals("DockerImage", version.getType());
        assertNull(version.getGroupId());
        assertNull(version.getArtifactId());
        assertEquals("1.0.1", version.getVersion());
    }

    @Test
    public void testUnmarshallingEmpty() throws Exception {
        String payload = "{ }";

        try {
            objectMapper.readValue(payload, Stage.class);
            fail();
        } catch (Exception e) {

        }
    }
}
