package io.artifactz.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.artifactz.client.exception.ClientException;
import io.artifactz.client.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.artifactz.client.FeedbackLevel.INFO;

public class ServiceClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    final String baseUrl;
    final String apiToken;
    final String proxyUrl;
    final String sender;
    final String proxyUsername;
    final String proxyPassword;
    final Feedback feedback;

    public ServiceClient(String baseUrl, String apiToken, String sender, String proxyUrl, String proxyUsername, String proxyPassword, Feedback feedback) {
        this.baseUrl = baseUrl;
        this.apiToken = apiToken;
        this.sender = sender;
        this.proxyUrl = proxyUrl;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.feedback = feedback;
    }

    public Stage retrieveVersions(String stage, String[] artifacts) throws ClientException {
        CloseableHttpClient client = HttpClients.createDefault();
        String artifactsQuery = Arrays.stream(artifacts).map(a -> "artifact=" + a).collect(Collectors.joining("&"));
        HttpGet getVersion = new HttpGet(this.baseUrl + "/stages/" + stage + "/list?" + artifactsQuery);

        try {
            this.setRequestProxy(getVersion);
        } catch (MalformedURLException e) {
            throw new ClientException("Incorrect proxy URL specified: " + this.proxyUrl);
        }

        getVersion.setHeader("Accepts", ContentType.APPLICATION_JSON.getMimeType());
        getVersion.setHeader("Authorization", "Bearer " + this.apiToken);
        if (this.sender != null) {
            getVersion.setHeader("X-ClientId", this.sender);
        }
        CloseableHttpResponse response = null;
        try {
            response = client.execute(getVersion);
            if (response.getStatusLine().getStatusCode() != 200) {
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
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPut publishRequest = new HttpPut(this.baseUrl + "/artifacts/versions");

        try {
            this.setRequestProxy(publishRequest);
        } catch (MalformedURLException e) {
            throw new ClientException("Incorrect proxy URL specified: " + this.proxyUrl);
        }

        try {
            this.prepareRequest(publishRequest, request);
        } catch (JsonProcessingException e) {
            throw new ClientException("Cannot create request", e);
        }

        CloseableHttpResponse response = null;
        try {
            response = client.execute(publishRequest);
            if (response.getStatusLine().getStatusCode() != 202) {
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

    public void pushArtifact(String stage, String name, String version) throws ClientException {
        this.sendMessage(INFO, "Pushing the artifact version '" + version + "' at the stage '" + stage + "'");
        this.sendMessage(INFO, "Performing PUT request to  to the Artifactor instance @" + this.baseUrl);
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

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut pushRequest = new HttpPut(this.baseUrl + "/artifacts/push");
        try {
            this.setRequestProxy(pushRequest);
        } catch (MalformedURLException e) {
            throw new ClientException("Incorrect proxy URL specified: " + this.proxyUrl);
        }

        try {
            this.prepareRequest(pushRequest, request);
        } catch (JsonProcessingException e) {
            throw new ClientException("Cannot create request", e);
        }

        CloseableHttpResponse response = null;

        try {
            response = client.execute(pushRequest);
            if (response.getStatusLine().getStatusCode() != 202) {
                this.handleError(response);
            } else {
                this.sendMessage(INFO, "Successfully pushed artifact version");
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
    }

    private void setRequestProxy(HttpRequestBase request) throws MalformedURLException  {
        String proxySchema;
        String proxyHost;
        int proxyPort;
        HttpHost proxyHttpHost = null;

        HttpClientBuilder clientbuilder = HttpClients.custom();

        if (!StringUtils.isEmpty(this.proxyUrl)) {
            URL proxyUri = new URL(this.proxyUrl);
            proxySchema = proxyUri.getProtocol();
            proxyHost = proxyUri.getHost();
            proxyPort = proxyUri.getPort();
            if (proxyPort == -1) {
                proxyPort = proxyUri.getDefaultPort();
            }
            proxyHttpHost = new HttpHost(proxyHost, proxyPort, proxySchema);

            if (!StringUtils.isEmpty(this.proxyUsername) && !StringUtils.isEmpty(this.proxyPassword)) {
                org.apache.http.client.CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(proxyHttpHost),
                        new UsernamePasswordCredentials(this.proxyUsername, this.proxyPassword));
                clientbuilder.setDefaultCredentialsProvider(credsProvider);
            }
        }

        if (proxyHttpHost != null) {
            RequestConfig.Builder reqconfigconbuilder = RequestConfig.custom();
            reqconfigconbuilder = reqconfigconbuilder.setProxy(proxyHttpHost);
            RequestConfig config = reqconfigconbuilder.build();
            request.setConfig(config);
        }
    }

    private void sendMessage(FeedbackLevel level, String message) {
        if (this.feedback != null) {
            this.feedback.send(level, message);
        }
    }

    private void prepareRequest(HttpEntityEnclosingRequest request, Request data) throws JsonProcessingException{
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
        if (response.getStatusLine().getStatusCode() == 401) {
            throw new ClientException("Unauthorized");
        }

        String contentType = response.getEntity().getContentType().toString();

        if (StringUtils.equals(contentType, ContentType.APPLICATION_JSON.getMimeType())) {
            ErrorMessage message = objectMapper.readValue(EntityUtils.toString(response.getEntity()), ErrorMessage.class);
            throw new ClientException(message.getError());
        } else {
            throw new ClientException("Unknown error");
        }
    }
}
