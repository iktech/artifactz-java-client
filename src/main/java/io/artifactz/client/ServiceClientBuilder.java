package io.artifactz.client;

import io.artifactz.client.exception.ClientException;

/**
 * Helper class that builds the client object
 */
public class ServiceClientBuilder {
    private String baseUrl;
    private String apiToken;
    private String proxyUrl;
    private String sender;
    private String proxyUsername;
    private String proxyPassword;
    private Feedback feedback;

    public static ServiceClientBuilder withBaseUrl(String baseUrl) {
        ServiceClientBuilder builder = new ServiceClientBuilder();
        builder.baseUrl = baseUrl;
        return builder;
    }

    public ServiceClientBuilder withApiToken(String apiToken) {
        this.apiToken = apiToken;
        return this;
    }

    public ServiceClientBuilder withProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
        return this;
    }

    public ServiceClientBuilder withSender(String sender) {
        this.sender = sender;
        return this;
    }

    public ServiceClientBuilder withProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
        return this;
    }

    public ServiceClientBuilder withProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    public ServiceClientBuilder provideFeedback(Feedback feedback) {
        this.feedback = feedback;
        return this;
    }

    public ServiceClient build() throws ClientException {
        if (this.baseUrl == null) {
            throw new ClientException("Service Base URL is required");
        }

        if (this.apiToken == null) {
            throw new ClientException("API Token is required");
        }

        return new ServiceClient(this.baseUrl, this.apiToken, this.sender, this.proxyUrl, this.proxyUsername, this.proxyPassword, this.feedback);
    }
}
