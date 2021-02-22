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
    public void testPushArtifactWithVersion() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        String version = client.pushArtifact("Development",
                "test-data", "1.0.0");
        assertEquals("1.0.0", version);
    }

    @Test
    public void testPushArtifact() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .withUserAgent("JUnit 5")
                .build();

        String version = client.pushArtifact("Development",
                "test-data");
        assertEquals("1.0.0", version);
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

        Stage stage = client.retrieveVersions("Development","test-data", "test-api");
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
    public void testRetrieveVersion() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Development", "test-data");
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
    public void testRetrieveVersionsJava() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveJavaVersions("Development", "io.iktech.test:test-data");
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
    public void testValidateConnection() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        client.validateConnection();
    }

    @Test
    public void testValidateConnectionInvalid() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl("https://iktech.io")
                    .withApiToken(TOKEN_READ_WRITE)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();

            client.validateConnection();
            fail();
        } catch (ClientException e) {
            assertEquals("Failed to validate connection to the Artifactor instance @https://iktech.io", e.getMessage());
        }
    }

    @Test
    public void testRetrieveVersionJava() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_WRITE)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveJavaVersions("Development", "io.iktech.test:test-data");
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

            client.retrieveVersions("Development", "test-data", "test-api");
            fail();
        } catch (ClientException e) {
            assertEquals("Failed to get data from the Artifactor Server", e.getMessage());
            assertEquals("Unauthorized", e.getCause().getMessage());
        }
    }

    @Test
    public void testRetrieveVersionsEmpty() throws Exception {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_ONLY)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Automated Integration Tests", "test-data", "test-api");

        assertNotNull(stage);
        assertEquals("Automated Integration Tests", stage.getStage());
        assertNull(stage.getArtifacts());
    }

    @Test
    public void testRetrieveVersionEmpty() throws Exception {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactor-uat.iktech.io")
                .withApiToken(TOKEN_READ_ONLY)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Automated Integration Tests", "test-data");

        assertNotNull(stage);
        assertEquals("Automated Integration Tests", stage.getStage());
        assertNull(stage.getArtifacts());
    }
}
