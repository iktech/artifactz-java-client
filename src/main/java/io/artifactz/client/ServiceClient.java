package io.artifactz.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.artifactz.client.exception.ClientException;
import io.artifactz.client.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.VersionInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.artifactz.client.FeedbackLevel.INFO;

/**
 * This class provides methods to interact with Artifactz.io service allowing to:
 *   - publish new artifact or the new version of the existing artifact
 *   - push artifact through the flow if artifact is associated with one
 *   - retrieve versions of the artifacts at a specific stage from the service
 */
public class ServiceClient {
    private static final String USER_AGENT = "Artifactz.io-client-library/1.1";

    final String baseUrl;
    final String apiToken;
    final String proxyUrl;
    final String sender;
    final String proxyUsername;
    final String proxyPassword;
    final String userAgent;
    final Feedback feedback;

    private final ObjectMapper objectMapper;

    /**
     * Service client constructor
     *
     * @param baseUrl service base Url
     * @param apiToken service API token
     * @param sender sender identifier
     * @param proxyUrl proxy server Url
     * @param proxyUsername proxy username
     * @param proxyPassword proxy password
     * @param userAgent user agent
     * @param feedback feedback interface implementation
     */
    public ServiceClient(String baseUrl, String apiToken, String sender, String proxyUrl, String proxyUsername, String proxyPassword, String userAgent, Feedback feedback, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.apiToken = apiToken;
        this.sender = sender;
        this.proxyUrl = proxyUrl;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.userAgent = userAgent;
        this.feedback = feedback;
        this.objectMapper = objectMapper;
    }

    /**
     * Retrieves only named artifacts from at the specified stage
     *
     * @param stage stage name
     * @param artifacts array of the artifact names to retrieve
     *
     * @return object containing the details of the found artifacts
     *
     * @throws ClientException in case of the any failure to get artifact details
     */
    public Stage retrieveVersions(String stage, String ...artifacts) throws ClientException {
        return retrieveVersions(stage, artifacts, null);
    }

    /**
     * Retrieves only java artifacts from at the specified stage
     *
     * @param stage stage name
     * @param javaArtifacts array of the java artifact names in the format groupId:artifactId to retrieve
     *
     * @return object containing the details of the found artifacts
     *
     * @throws ClientException in case of any failure to get artifact details
     */
    public Stage retrieveJavaVersions(String stage, String ...javaArtifacts) throws ClientException {
        return retrieveVersions(stage, null, javaArtifacts);
    }

    /**
     * Retrieves artifacts from at the specified stage
     *
     * @param stage stage name
     * @param artifacts array of the artifact names to retrieve
     * @param javaArtifacts array of the java artifact names in the format groupId:artifactId to retrieve
     *
     * @return object containing the details of the found artifacts
     *
     * @throws ClientException in case of the any failure to get artifact details
     */
    public Stage retrieveVersions(String stage, String[] artifacts, String[] javaArtifacts) throws ClientException {
        CloseableHttpClient client = this.getHttpClient();

        if (artifacts == null) {
            artifacts = new String[]{};
        }

        if (javaArtifacts == null) {
            javaArtifacts = new String[]{};
        }


        String artifactsQuery = Stream.concat(Arrays.stream(artifacts).map(a -> "artifact=" + a), Arrays.stream(javaArtifacts).map(a -> "java_artifact=" + a)).collect(Collectors.joining("&"));
        HttpGet getVersion = new HttpGet(this.baseUrl + "/stages/" + stage.replace(" ", "%20") + "/list?" + artifactsQuery);

        getVersion.setHeader("Accepts", ContentType.APPLICATION_JSON.getMimeType());
        getVersion.setHeader("Authorization", "Bearer " + this.apiToken);
        if (this.sender != null) {
            getVersion.setHeader("X-ClientId", this.sender);
        }
        CloseableHttpResponse response = null;
        try {
            response = client.execute(getVersion);
            if (response.getCode() != 200) {
                this.handleError(response);
            } else {
                return objectMapper.readValue(EntityUtils.toString(response.getEntity()), Stage.class);
            }
        } catch (Exception e) {
            throw new ClientException("Failed to get data from the Artifactor Server", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {}
            }

            try {
                client.close();
            } catch (IOException e) {}
        }

        return null;
    }

    /**
     * Publishes artifact version to a specified stage. If stage does not exist service will automatically creates it.
     *
     * @param stage stage name
     * @param stageDescription stage description
     * @param name artifact name
     * @param description artifact description
     * @param flow flow name
     * @param type artifact type
     * @param groupId maven group Id (for java artifacts only)
     * @param artifactId maven artifact Id (for java artifacts only)
     * @param version artifact version
     * @throws ClientException if method fails to publish artifact
     */
    public void publishArtifact(String stage, String stageDescription, String name, String description, String flow, String type, String groupId, String artifactId, String version) throws ClientException {
        this.sendMessage(INFO, "Patching the artifact version details at the stage '" + stage + "' to the Artifactor instance @ " + this.baseUrl);
        this.sendMessage(INFO, "Artifact details:");
        this.sendMessage(INFO, "  name: " + name);
        if (!StringUtils.isEmpty(description)) {
            this.sendMessage(INFO, "  description: " + description);
        }

        if (!StringUtils.isEmpty(groupId)) {
            this.sendMessage(INFO, "  group Id: " + groupId);
        }
        if (!StringUtils.isEmpty(artifactId)) {
            this.sendMessage(INFO, "  artifact Id: " + artifactId);
        }
        this.sendMessage(INFO, "  version: " + version);
        PublishVersionRequest request = new PublishVersionRequest();
        request.setStage(stage);
        request.setFlow(flow);
        request.setStageDescription(stageDescription);
        request.setName(name);
        request.setDescription(description);
        request.setType(type);
        request.setGroupId(groupId);
        request.setArtifactId(artifactId);
        request.setVersion(version);
        CloseableHttpClient client = this.getHttpClient();

        HttpPut publishRequest = new HttpPut(this.baseUrl + "/artifacts/versions");

        try {
            this.prepareRequest(publishRequest, request);
        } catch (JsonProcessingException e) {
            throw new ClientException("Cannot create request", e);
        }

        CloseableHttpResponse response = null;
        try {
            response = client.execute(publishRequest);
            if (response.getCode() != 202) {
                this.handleError(response);
            } else {
                this.sendMessage(INFO, "Successfully patched artifact version");
            }
        } catch (Exception e) {
            throw new ClientException("Failed to send artifact details to the Artifactor Server", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {}
            }

            try {
                client.close();
            } catch (IOException e) {}
        }
    }


    /**
     * Pushes artifact through flow without specifying the version, i.e. pushes the current version at the specified
     * stage
     *
     * @param stage stage name from where artifact version should be pushed
     * @param name name of the artifact to push
     * @return version that was pushed
     * @throws ClientException if error occurs during attempt to push artifact
     */
    public String pushArtifact(String stage, String name) throws ClientException {
        return pushArtifact(stage, name, null);
    }

    /**
     * Pushes artifact through flow
     *
     * @param stage stage name from where artifact version should be pushed
     * @param name name of the artifact to push
     * @param version artifact version to push
     * @return version that was pushed
     * @throws ClientException if error occurs during attempt to push artifact
     */
    public String pushArtifact(String stage, String name, String version) throws ClientException {
        this.sendMessage(INFO, "Pushing the artifact version '" + version + "' at the stage '" + stage + "'");
        this.sendMessage(INFO, "Performing PUT request to the Artifactor instance @" + this.baseUrl);
        this.sendMessage(INFO, "Artifact details:");
        this.sendMessage(INFO, "  name: " + name);
        this.sendMessage(INFO, "  stage: " + stage);
        if (version != null) {
            this.sendMessage(INFO, "  version: " + version);
        }

        PushVersionRequest request = new PushVersionRequest();
        request.setStage(stage);
        request.setName(name);
        if (version != null) {
            request.setVersion(version);
        }

        CloseableHttpClient client = this.getHttpClient();

        HttpPut pushRequest = new HttpPut(this.baseUrl + "/artifacts/push");

        try {
            this.prepareRequest(pushRequest, request);
        } catch (JsonProcessingException e) {
            throw new ClientException("Cannot create request", e);
        }

        CloseableHttpResponse response = null;

        try {
            response = client.execute(pushRequest);
            if (response.getCode() != 200) {
                this.handleError(response);
            } else {
                this.sendMessage(INFO, "Successfully pushed artifact version");
                PushVersionRequest pushVersionResponse = objectMapper.readValue(EntityUtils.toString(response.getEntity()), PushVersionRequest.class);
                if (pushVersionResponse != null) {
                    return pushVersionResponse.getVersion();
                }
            }
        } catch (Exception e) {
            throw new ClientException("Failed to push artifact version", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {}
            }

            try {
                client.close();
            } catch (IOException e) {}
        }

        return null;
    }

    /**
     * Validates client connection to the service instance
     *
     * @throws ClientException if error occurs during attempt to validate connection
     */
    public void validateConnection() throws ClientException {
        this.sendMessage(INFO, "Validating connection to the Artifactor instance @" + this.baseUrl);
        CloseableHttpClient client = this.getHttpClient();
        HttpGet validateConnection = new HttpGet(this.baseUrl + "/validate");

        validateConnection.setHeader("Accepts", ContentType.APPLICATION_JSON.getMimeType());
        validateConnection.setHeader("Authorization", "Bearer " + this.apiToken);
        if (this.sender != null) {
            validateConnection.setHeader("X-ClientId", this.sender);
        }

        CloseableHttpResponse response = null;

        try {
            response = client.execute(validateConnection);
            if (response.getCode() != 200) {
                this.handleError(response);
            } else {
                this.sendMessage(INFO, "Successfully validated connection");
            }
        } catch (Exception e) {
            throw new ClientException("Failed to validate connection to the Artifactor instance @" + this.baseUrl, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {}
            }

            try {
                client.close();
            } catch (IOException e) {}
        }

    }

    private void sendMessage(FeedbackLevel level, String message) {
        if (this.feedback != null) {
            this.feedback.send(level, message);
        }
    }

    private void prepareRequest(HttpUriRequestBase request, Request data) throws JsonProcessingException{
        String content = objectMapper.writeValueAsString(data);
        StringEntity entity = new StringEntity(content, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        request.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        request.setHeader("Accepts", ContentType.APPLICATION_JSON.getMimeType());
        if (this.sender != null) {
            request.setHeader("X-ClientId", this.sender);
        }

        request.setHeader("Authorization", "Bearer " + this.apiToken);
    }

    private void handleError(CloseableHttpResponse response) throws IOException, ClientException {
        if (response.getCode() == 401) {
            throw new ClientException("Unauthorized");
        }

        if (response.getCode() == 403) {
            throw new ClientException("Forbidden");
        }

        String contentType = response.getEntity().getContentType();

        if (StringUtils.equals(contentType, ContentType.APPLICATION_JSON.getMimeType())) {
            try {
                ErrorMessage message = objectMapper.readValue(EntityUtils.toString(response.getEntity()), ErrorMessage.class);
                throw new ClientException(message.getError());
            } catch (ParseException e) {
                throw new ClientException("Cannot parse response object");
            }
        } else {
            throw new ClientException("Unknown error");
        }
    }

    private CloseableHttpClient getHttpClient() throws ClientException {
        String proxySchema;
        String proxyHost;
        int proxyPort;
        HttpHost proxyHttpHost = null;

        HttpClientBuilder clientbuilder = HttpClients.custom();

        if (!StringUtils.isEmpty(this.proxyUrl)) {
            try {
                URL proxyUri = new URL(this.proxyUrl);
                proxySchema = proxyUri.getProtocol();
                proxyHost = proxyUri.getHost();
                proxyPort = proxyUri.getPort();
                if (proxyPort == -1) {
                    proxyPort = proxyUri.getDefaultPort();
                }
                proxyHttpHost = new HttpHost(proxySchema, proxyHost, proxyPort);

                if (!StringUtils.isEmpty(this.proxyUsername) && !StringUtils.isEmpty(this.proxyPassword)) {
                    BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
                    credsProvider.setCredentials(new AuthScope(proxyHttpHost),
                            new UsernamePasswordCredentials(this.proxyUsername, this.proxyPassword.toCharArray()));
                    clientbuilder.setDefaultCredentialsProvider(credsProvider);
                }

                HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHttpHost);
                clientbuilder.setRoutePlanner(routePlanner);
            } catch (MalformedURLException e) {
                throw new ClientException("Proxy URL is malformed: " + this.proxyUrl);
            }
        }

        clientbuilder.setUserAgent((this.userAgent != null ? this.userAgent + " " : "") + USER_AGENT + " " + VersionInfo.getSoftwareInfo("Apache-HttpClient",
                "org.apache.http.client", getClass()));

        return clientbuilder.build();
    }
}
