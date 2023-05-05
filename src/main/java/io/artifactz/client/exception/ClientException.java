package io.artifactz.client.exception;

/**
 * This is client exception which could be raised by the ServiceClient, ServiceClientBuilder
 *
 * @see io.artifactz.client.ServiceClient
 * @see io.artifactz.client.ServiceClientBuilder
 */
public class ClientException extends Exception {
    /**
     * Default constructor that sets no message
     */
    public ClientException() {
        super();
    }

    /**
     * Override constructor with the custom message
     * @param message exception message
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * Another override constructor with the custom message and the parent exception
     * @param message exception message
     * @param cause an exception that caused the ClientException
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
