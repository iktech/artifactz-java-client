package io.artifactz.client;

import io.artifactz.client.exception.ClientException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceClientBuilderTest {
    @Test
    public void testClientBuilder() throws Exception {
        Feedback feedback = new FeedbackImpl();
        ServiceClient client = ServiceClientBuilder
                .withBaseUrl("https://artifactz-uat.iktech.io")
                .withApiToken("2cf2363e-4551-43ec-abfd-facfffb17493")
                .withSender("unit-test")
                .withProxyUrl("http://proxy.iktech.io:3128")
                .withProxyUsername("test")
                .withProxyPassword("password")
                .provideFeedback(feedback).build();
        assertNotNull(client);
        assertEquals("https://artifactz-uat.iktech.io", client.baseUrl);
        assertEquals("2cf2363e-4551-43ec-abfd-facfffb17493", client.apiToken);
        assertEquals("unit-test", client.sender);
        assertEquals("http://proxy.iktech.io:3128", client.proxyUrl);
        assertEquals("test", client.proxyUsername);
        assertEquals("password", client.proxyPassword);
        assertNotNull(client.feedback);
        assertTrue(client.feedback instanceof FeedbackImpl);
    }

    @Test
    public void testClientBuilderProduction() throws Exception {
        Feedback feedback = new FeedbackImpl();
        ServiceClient client = ServiceClientBuilder
                .create()
                .withApiToken("2cf2363e-4551-43ec-abfd-facfffb17493")
                .withSender("unit-test")
                .provideFeedback(feedback).build();
        assertNotNull(client);
        assertEquals("https://artifactor.artifactz.io", client.baseUrl);
        assertEquals("2cf2363e-4551-43ec-abfd-facfffb17493", client.apiToken);
        assertEquals("unit-test", client.sender);
        assertNull(client.proxyUrl);
        assertNull(client.proxyUsername);
        assertNull(client.proxyPassword);
        assertNotNull(client.feedback);
        assertTrue(client.feedback instanceof FeedbackImpl);
    }

    @Test
    public void testClientBuilderWithNoUrl() {
        try {
            ServiceClientBuilder
                    .withBaseUrl(null).build();
            fail();
        } catch (ClientException e) {
            assertEquals("Service Base URL is required", e.getMessage());
        }
    }

    @Test
    public void testClientBuilderWithNoApiToken() {
        try {
            ServiceClientBuilder
                    .withBaseUrl("https://artifactz-uat.iktech.io")
                    .build();
            fail();
        } catch (ClientException e) {
            assertEquals("API Token is required", e.getMessage());
        }
    }

    class FeedbackImpl implements Feedback {
        @Override
        public void send(FeedbackLevel level, String message) {

        }
    }
}
