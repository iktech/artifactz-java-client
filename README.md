# Artifactz.io client library
![Build](https://github.com/iktech/artifactz-java-client/workflows/Build/badge.svg)

Artifactz.io is a service helping users to track project artifacts through
the different stages of their lifecycle.

This library provides helper methods to access artifactz.io service API to publish new artifact data,
push the artifact through the flow or get the version data from the service.

## Usage
Create a client using the ServiceClientBuilder.
BaseUrl and ApiToken are the mandatory client properties. The others are optional.
```java
            ServiceClient client = ServiceClientBuilder
                    .withBaseUrl("https://artifactor.artifactz.io")
                    .withApiToken("<your access token here>")
                    .provideFeedback(new FeedbackImpl())
                    .withSender("sender")
                    .build();

```

**Service Client Properties**


Name | Description |
---|---
baseUrl | Service base url. In production it should always be https://artifactor.artifactz.io
apiToken | API Token to access service with permissions to publish or read depending on the intended usage
proxyUrl | the URL of the proxy service (optional)
proxyUsername | proxy username if proxy is used and it requires authentication (optional)
proxyPassword | proxy password (optional)
sender | Sender identifier, recorded by the service along with the version (optional)
feedback | the customer implementation of the Feedback interface to report the progress and/or client errors

To publish artifact details:

```java
        client.publishArtifact("Development", // Stage name
                        "Development Stage", // Stage description
                        "test-data", // Artifact name
                        "Test Data Library", //Artifact description
                        "Test", // Optional flow name
                        "JAR", // Artifact type
                        "io.iktech.test", // Group Id (optional, only for java artifacts)
                        "test-data", // Artifact Id (optional, only for java artifacts)
                        "1.0.0"); // Artifact version
```

To push artifact through the flow:
```java
        client.pushArtifact("Development", // Stage from where the artifact gets pushed
                "test-data", // Artifact name
                "1.0.0");  // Artifact version to push
```

To retrieve artifact details at a particular stage:
```java
        Stage stage = client.retrieveVersions("Development", // Stage from where to get version info
            new String[]{"test-data", "test-api"}); // Array of the artifact names
```

## License

See [LICENSE](LICENSE).

## Copyright

Copyright (c) 2020 - 2021 IKtech Limited
