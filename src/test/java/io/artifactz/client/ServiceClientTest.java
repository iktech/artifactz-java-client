package io.artifactz.client;

import io.artifactz.client.exception.ClientException;
import io.artifactz.client.model.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceClientTest {
    private static final String TOKEN_READ_WRITE = "2b7f0fa0-343a-4a1f-8015-e7801b4152fe";
    private static final String TOKEN_READ_ONLY = "ae355d37-0bf6-4f8b-990f-5b0ffa5416e0";
    private static final String TOKEN_WRITE_ONLY = "e7bf5f6e-bde9-4ae5-b01c-077d7935c691";

    @Test
    public void testPublishArtifact() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        client.publishArtifact("Development",
                "Development Stage",
                "test-data",
                "Test Data Library",
                "Test",
                "JAR",
                "io.iktech.test",
                "test-data",
                "1.0.0");
    }

    @Test
    public void testPublishArtifactIncorrectToken() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl("https://artifactor-uat.iktech.io")
                    .withApiToken(TOKEN_READ_ONLY)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();

            client.publishArtifact("Development",
                    "Development Stage",
                    "test-data",
                    "Test Data Library",
                    "Test",
                    "JAR",
                    "io.iktech.test",
                    "test-data",
                    "1.0.0");
            fail();
        } catch (ClientException e) {
            assertEquals("Failed to send artifact details to the Artifactor Server", e.getMessage());
            assertEquals("Unauthorized", e.getCause().getMessage());
        }
    }

    @Test
    public void testPushArtifact() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        client.pushArtifact("Development",
                "test-data",
                "1.0.0");
    }

    @Test
    public void testPushArtifactIncorrectToken() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl("https://artifactor-uat.iktech.io")
                    .withApiToken(TOKEN_READ_ONLY)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();

            client.pushArtifact("Development",
                    "test-data",
                    "1.0.0");
            fail();
        } catch (ClientException e) {
            assertEquals("Failed to push artifact version", e.getMessage());
            assertEquals("Unauthorized", e.getCause().getMessage());
        }
    }

    @Test
    public void testRetrieveVersions() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Development",
                new String[]{"test-data", "test-api"});
        assertNotNull(stage);
        assertEquals("Development", stage.getStage());
        assertEquals(1, stage.getArtifacts().size());
        assertEquals("test-data", stage.getArtifacts().get(0).getArtifactName());
        assertEquals("Test Data Library", stage.getArtifacts().get(0).getArtifactDescription());
        assertEquals("JAR", stage.getArtifacts().get(0).getType());
        assertEquals("io.iktech.test", stage.getArtifacts().get(0).getGroupId());
        assertEquals("test-data", stage.getArtifacts().get(0).getArtifactId());
        assertEquals("1.0.0", stage.getArtifacts().get(0).getVersion());
    }

    @Test
    public void testRetrieveVersionsIncorrectToken() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl("https://artifactor-uat.iktech.io")
                    .withApiToken(TOKEN_WRITE_ONLY)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();

            client.retrieveVersions("Development",
                    new String[]{"test-data", "test-api"});
            fail();
        } catch (ClientException e) {
            assertEquals("Failed to get data from the Artifactor Server", e.getMessage());
            assertEquals("Unauthorized", e.getCause().getMessage());
        }
    }
}
