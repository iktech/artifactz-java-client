package io.artifactz.client.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorMessageTest extends JSONTest {
    @Test
    public void testMarshalling() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setError("This is a test");

        String jsonString = objectMapper.writeValueAsString(errorMessage);
        assertEquals("This is a test", JsonPath.read(jsonString, "$.error"));
    }

    @Test
    public void testMarshallingEmpty() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage();

        String jsonString = objectMapper.writeValueAsString(errorMessage);
        DocumentContext ctx = JsonPath.parse(jsonString);
        assertTrue(assertDoesNotExist(ctx, "$.error"));
    }

    @Test
    public void testUnmarshalling() throws Exception {
        String payload = "{ \"error\": \"This is a test error\"}";

        ErrorMessage errorMessage = objectMapper.readValue(payload, ErrorMessage.class);
        assertEquals("This is a test error", errorMessage.getError());
    }

    @Test
    public void testUnmarshallingEmpty() throws Exception {
        String payload = "{ }";

        try {
            objectMapper.readValue(payload, ErrorMessage.class);
            fail();
        } catch (Exception e) {

        }
    }
}
