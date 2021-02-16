package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublishVersionRequest extends Request {
    private static final long serialVersionUID = 2207419267657211214L;

    @JsonProperty("stage")
    private String stage;
    @JsonProperty("flow")
    private String flow;
    @JsonProperty("stage_description")
    private String stageDescription;
    @JsonProperty("artifact_name")
    private String name;
    @JsonProperty("artifact_description")
    private String description;
    @JsonProperty("type")
    private String type;
    @JsonProperty("group_id")
    private String groupId;
    @JsonProperty("artifact_id")
    private String artifactId;
    @JsonProperty("version")
    private String version;

    public PublishVersionRequest() {

    }

    @JsonCreator
    public PublishVersionRequest(@JsonProperty(value = "stage", required = true) String stage,
                                 @JsonProperty(value = "flow") String flow,
                                 @JsonProperty(value = "stage_description", required = true) String stageDescription,
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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    @JsonGetter("stage_description")
    public String getStageDescription() {
        return stageDescription;
    }

    public void setStageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
    }

    @JsonGetter("artifact_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("artifact_description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonGetter("group_id")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @JsonGetter("artifact_id")
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
