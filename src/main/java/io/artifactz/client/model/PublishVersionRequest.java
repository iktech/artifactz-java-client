package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The JSON message for the artifact publishing to the artifactz.io service
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublishVersionRequest extends Request {
    private static final long serialVersionUID = 2207419267657211214L;

    /**
     * Stage Name
     */
    @JsonProperty("stage")
    private String stage;

    /**
     * Stage description
     */
    @JsonProperty("stage_description")
    private String stageDescription;

    /**
     * Flow name
     */
    @JsonProperty("flow")
    private String flow;

    /**
     * Artifact name
     */
    @JsonProperty("artifact_name")
    private String name;

    /**
     * Artifact description
     */
    @JsonProperty("artifact_description")
    private String description;

    /**
     * Artifact type
     */
    @JsonProperty("type")
    private String type;

    /**
     * Maven group Id
     */
    @JsonProperty("group_id")
    private String groupId;

    /**
     * Maven artifact Id
     */
    @JsonProperty("artifact_id")
    private String artifactId;

    /**
     * Artifact version
     */
    @JsonProperty("version")
    private String version;

    /**
     * Default constructor that creates an empty message
     */
    public PublishVersionRequest() {

    }

    /**
     * Another constructore allowing to set all the message fields
     *
     * @param stage stage where to register the artifact
     * @param stageDescription stage description
     * @param flow flow name
     * @param name artifact name
     * @param description artifact description
     * @param type artifact type, currently supported values are "JAR", "WAR", "EAR" and "DockerImage"
     * @param groupId maven group Id for the Java artifacts (JAR/WAR/EAR)
     * @param artifactId maven artifact Id for the Java artifacts (JAR/WAR/EAR)
     * @param version a version to register
     */
    @JsonCreator
    public PublishVersionRequest(@JsonProperty(value = "stage", required = true) String stage,
                                 @JsonProperty(value = "stage_description", required = true) String stageDescription,
                                 @JsonProperty(value = "flow") String flow,
                                 @JsonProperty(value = "artifact_name", required = true) String name,
                                 @JsonProperty("artifact_description") String description,
                                 @JsonProperty(value = "type", required = true) String type,
                                 @JsonProperty("group_id") String groupId,
                                 @JsonProperty("artifact_id") String artifactId,
                                 @JsonProperty(value = "version", required = true) String version) {
        this.stage = stage;
        this.flow = flow;
        this.stageDescription = stageDescription;
        this.name = name;
        this.description = description;
        this.type = type;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * Returns the stage name
     *
     * @return stage name
     */
    public String getStage() {
        return stage;
    }

    /**
     * Sets the new stage name
     *
     * @param stage stage name
     */
    public void setStage(String stage) {
        this.stage = stage;
    }

    /**
     * Returns the flow name
     *
     * @return flow name
     */
    @JsonGetter("flow")
    public String getFlow() {
        return flow;
    }

    /**
     * Set the new flow name
     *
     * @param flow flow name
     */
    public void setFlow(String flow) {
        this.flow = flow;
    }

    /**
     * Returns the stage description
     *
     * @return stage description
     */
    @JsonGetter("stage_description")
    public String getStageDescription() {
        return stageDescription;
    }

    /**
     * Sets new stage description
     *
     * @param stageDescription stage description
     */
    public void setStageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
    }

    /**
     * Returns the artifact name
     *
     * @return artifact name
     */
    @JsonGetter("artifact_name")
    public String getName() {
        return name;
    }

    /**
     * Sets the new artiface name
     *
     * @param name artifact name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the artifact description
     *
     * @return artifact description
     */
    @JsonGetter("artifact_description")
    public String getDescription() {
        return description;
    }

    /**
     * Sets the new artifact description
     *
     * @param description artifact description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the artifact type.
     *
     * @return artifact type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the new artifact type
     *
     * @param type artifact type, accepted values are "JAR", "WAR", "EAR" and "DockerImage"
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the java maven artifact group Id. By Java artifacts we understand those with the type
     * "JAR", "WAR" or "EAR"
     *
     * @return group Id
     */
    @JsonGetter("group_id")
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets the new java maven artifact group Id.
     *
     * @param groupId group Id
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * Returns the java maven artifact Id. By Java artifacts we understand those with the type
     * "JAR", "WAR" or "EAR"
     *
     * @return artifact Id
     */
    @JsonGetter("artifact_id")
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Sets the new java maven Artifact Id
     *
     * @param artifactId artifact Id
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Returns the artifact version
     *
     * @return artifact version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets new artifact version
     *
     * @param version artifact version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
