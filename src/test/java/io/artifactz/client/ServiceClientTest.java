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
            url = "https://artifactor.uat.artifactz.io";
        }
        readWriteToken = System.getProperty("readWriteToken");
        readOnlyToken = System.getProperty("readOnlyToken");
        writeOnlyToken = System.getProperty("writeOnlyToken");
    }

    @Test
    @Order(1)
    public void testPublishArtifact() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
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
    @Order(2)
    public void testArtifactIncorrectToken() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl(url)
                    .withApiToken(readOnlyToken)
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
    @Order(3)
    public void testPushArtifactWithVersion() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        String version = client.pushArtifact("Development",
                "test-data", "1.0.0");
        assertEquals("1.0.0", version);
    }

    @Test
    @Order(4)
    public void testPushArtifact() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .withUserAgent("JUnit 5")
                .build();

        String version = client.pushArtifact("Development",
                "test-data");
        assertEquals("1.0.0", version);
    }

    @Test
    @Order(5)
    public void testPushArtifactIncorrectToken() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl(url)
                    .withApiToken(readOnlyToken)
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
    @Order(6)
    public void testRetrieveVersions() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
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
    @Order(7)
    public void testRetrieveVersion() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
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
    @Order(8)
    public void testRetrieveVersionsJava() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
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
    @Order(9)
    public void testValidateConnection() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        client.validateConnection();
    }

    @Test
    @Order(10)
    public void testValidateConnectionInvalid() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl("https://iktech.io")
                    .withApiToken(readWriteToken)
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
    @Order(11)
    public void testRetrieveVersionJava() throws ClientException {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readWriteToken)
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
    @Order(12)
    public void testRetrieveVersionsIncorrectToken() {
        try {
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl(url)
                    .withApiToken(writeOnlyToken)
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
    @Order(13)
    public void testRetrieveVersionsEmpty() throws Exception {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readOnlyToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Automated Integration Tests", "test-data", "test-api");

        assertNotNull(stage);
        assertEquals("Automated Integration Tests", stage.getStage());
        assertNull(stage.getArtifacts());
    }

    @Test
    @Order(14)
    public void testRetrieveVersionEmpty() throws Exception {
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl(url)
                .withApiToken(readOnlyToken)
                .provideFeedback(new UnitTestFeedback())
                .withSender("init-test")
                .build();

        Stage stage = client.retrieveVersions("Automated Integration Tests", "test-data");

        assertNotNull(stage);
        assertEquals("Automated Integration Tests", stage.getStage());
        assertNull(stage.getArtifacts());
    }
}
