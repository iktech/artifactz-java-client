package io.artifactz.client;

import io.artifactz.client.exception.ClientException;
import io.artifactz.client.model.Stage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceClientTest {
    private static String url;
    private static String readWriteToken;
    private static String readOnlyToken;
    private static String writeOnlyToken;

    @BeforeAll
    public static void prepare() {
        url = System.getProperty("serviceUrl");
        if (url == null) {
            url = "https://api.uat.artifactz.io";
        }
        readWriteToken = System.getProperty("readWriteToken");
        readOnlyToken = System.getProperty("readOnlyToken");
        writeOnlyToken = System.getProperty("writeOnlyToken");
    }

    @Test
    @Order(1)
    public void testPublishArtifactFailure() throws ClientException {
        try {
            ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();

            client.publishArtifact("Development",
                    "Development Stage",
                    "test-data",
                    "Test Data Library",
                    "Test",
                    "JAR",
                    null, null,
                    "1.0.0");
        } catch (ClientException e) {
            assertEquals("Failed to send artifact details to the Artifactor Server: group_id cannot be empty for the Java Artifact", e.getMessage());
            assertEquals("group_id cannot be empty for the Java Artifact", e.getCause().getMessage());
        }
    }

    @Test
    @Order(2)
    public void testPublishArtifact() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
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
    @Order(3)
    public void testArtifactIncorrectToken() {
        try {
            ServiceClient client = new ServiceClientBuilder(url, readOnlyToken)
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
            assertEquals("Failed to send artifact details to the Artifactor Server: Forbidden: insufficient scope", e.getMessage());
            assertEquals("Forbidden: insufficient scope", e.getCause().getMessage());
        }
    }

    @Test
    @Order(4)
    public void testPushArtifactWithVersion() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        String version = client.pushArtifact("Development",
                "test-data", "1.0.0");
        assertEquals("1.0.0", version);
    }

    @Test
    @Order(5)
    public void testPushArtifact() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .withUserAgent("JUnit 5")
                .build();

        String version = client.pushArtifact("Development",
                "test-data");
        assertEquals("1.0.0", version);
    }

    @Test
    @Order(6)
    public void testPushArtifactIncorrectToken() {
        try {
            ServiceClient client = new ServiceClientBuilder(url, readOnlyToken)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();

            client.pushArtifact("Development",
                    "test-data",
                    "1.0.0");
            fail();
        } catch (ClientException e) {
            assertEquals("Failed to push artifact version: Forbidden: insufficient scope", e.getMessage());
            assertEquals("Forbidden: insufficient scope", e.getCause().getMessage());
        }
    }

    @Test
    @Order(7)
    public void testRetrieveVersions() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
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
    @Order(8)
    public void testRetrieveVersion() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
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
    @Order(9)
    public void testRetrieveVersionsJava() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
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
    @Order(10)
    public void testValidateConnection() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        client.validateConnection();
    }

    @Test
    @Order(11)
    public void testValidateConnectionInvalid() {
        try {
            ServiceClient client = new ServiceClientBuilder("https://iktech.io", readWriteToken)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();

            client.validateConnection();
            fail();
        } catch (ClientException e) {
            assertEquals("Failed to validate connection to the Artifactor instance @https://iktech.io: Unknown error", e.getMessage());
        }
    }

    @Test
    @Order(11)
    public void testRetrieveVersionJava() throws ClientException {
        ServiceClient client = new ServiceClientBuilder(url, readWriteToken)
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
    @Order(13)
    public void testRetrieveVersionsIncorrectToken() {
        ClientException e = assertThrows(ClientException.class, () -> {
            ServiceClient client = new ServiceClientBuilder(url, writeOnlyToken)
                    .provideFeedback(new UnitTestFeedback())
                    .withSender("init-test")
                    .build();
            client.retrieveVersions("Development", "test-data", "test-api");
        });
        assertEquals("Failed to get data from the Artifactor Server: Forbidden: insufficient scope", e.getMessage());
        assertEquals("Forbidden: insufficient scope", e.getCause().getMessage());
    }

    @Test
    @Order(14)
    public void testRetrieveVersionsEmpty() throws Exception {
        ServiceClient client = new ServiceClientBuilder(url, readOnlyToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Automated Integration Tests", "test-data", "test-api");

        assertNotNull(stage);
        assertEquals("Automated Integration Tests", stage.getStage());
        assertNotNull(stage.getArtifacts());
        assertEquals(0, stage.getArtifacts().size());
    }

    @Test
    @Order(15)
    public void testRetrieveVersionEmpty() throws Exception {
        ServiceClient client = new ServiceClientBuilder(url, readOnlyToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Automated Integration Tests", "test-data");

        assertNotNull(stage);
        assertEquals("Automated Integration Tests", stage.getStage());
        assertNotNull(stage.getArtifacts());
        assertEquals(0, stage.getArtifacts().size());
    }
}
