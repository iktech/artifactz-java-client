package io.artifactz.client;

import io.artifactz.client.exception.ClientException;

/**
 * This class helps to configure and build the ServiceClient object.
 * At the minimum the ServiceClient could be built with the following code:
 *
 * <pre>
 *     {@code
        ServiceClient client = ServiceClientBuilder
            .create()
            .withApiToken("2cf2363e-4551-43ec-abfd-facfffb17493")
            .build();
 *     }
 * </pre>
 * Or if the client is used against the non production environment:
 * <pre>
 *     {@code
        ServiceClient client = ServiceClientBuilder
            .withBaseUrl("https://artifactz-uat.iktech.io")
            .withApiToken("2cf2363e-4551-43ec-abfd-facfffb17493")
            .build();
 *     }
 * </pre>
 *
 * @see ServiceClient
 */
public class ServiceClientBuilder {
    /**
     * The artifactz.io service base Url. For production use it must be https://artifactor.artifactz.io
     */
    private String baseUrl;

    /**
     * Valid Artifactz.io Api Token
     */
    private String apiToken;

    /**
     * Proxy server Url if required
     */
    private String proxyUrl;

    /**
     * Sender identifier
     */
    private String sender;

    /**
     * Proxy username
     */
    private String proxyUsername;

    /**
     * Proxy password
     */
    private String proxyPassword;

    /**
     * User agent externally set by the user program
     */
    private String userAgent;

    /**
     * Feedback interface implementation
     */
    private Feedback feedback;

    /**
     * Constructs ServiceClientBuilder with the base Url
     *
     * @param baseUrl service base url
     *
     * @return builder object for further configuration
     */
    public static ServiceClientBuilder withBaseUrl(String baseUrl) {
        ServiceClientBuilder builder = new ServiceClientBuilder();
        builder.baseUrl = baseUrl;
        return builder;
    }

    /**
     * Constructs ServiceClientBuilder pointing to the production instance
     *
     * @return builder object for further configuration
     */
    public static ServiceClientBuilder create() {
        ServiceClientBuilder builder = new ServiceClientBuilder();
        builder.baseUrl = "https://artifactor.artifactz.io";
        return builder;
    }

    /**
     * Adds the client Api token to configuration
     *
     * @param apiToken Api token to use
     *
     * @return configured builder object
     */
    public ServiceClientBuilder withApiToken(String apiToken) {
        this.apiToken = apiToken;
        return this;
    }

    /**
     * Adds proxy url to configuration
     *
     * @param proxyUrl proxy server url
     *
     * @return configured builder object
     */
    public ServiceClientBuilder withProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
        return this;
    }

    /**
     * Adds sender identifier to configuration
     *
     * @param sender sender identifier
     *
     * @return configured builder object
     */
    public ServiceClientBuilder withSender(String sender) {
        this.sender = sender;
        return this;
    }

    /**
     * Adds proxy username to configuration
     *
     * @param proxyUsername proxy username
     *
     * @return configured builder object
     */
    public ServiceClientBuilder withProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
        return this;
    }

    /**
     * Adds proxy password
     *
     * @param proxyPassword proxy password
     *
     * @return configured builder object
     */
    public ServiceClientBuilder withProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    /**
     * Adds user agent to the client
     *
     * @param userAgent user agent
     *
     * @return configured builder object
     */
    public ServiceClientBuilder withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Adds Feedback interface implementation
     *
     * @param feedback feedback interface implementation
     *
     * @return configured builder object
     */
    public ServiceClientBuilder provideFeedback(Feedback feedback) {
        this.feedback = feedback;
        return this;
    }

    /**
     * Builds ServiceClient object using specified configuration
     *
     * @return configured service cleitn
     *
     * @throws ClientException when configuration is incomplete, i.e. when baseUrl or Api token are not specified
     */
    public ServiceClient build() throws ClientException {
        if (this.baseUrl == null) {
            throw new ClientException("Service Base URL is required");
        }

        if (this.apiToken == null) {
            throw new ClientException("API Token is required");
        }

        return new ServiceClient(this.baseUrl, this.apiToken, this.sender, this.proxyUrl, this.proxyUsername, this.proxyPassword, this.userAgent, this.feedback);
    }
}
