package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The JSON message for the artifact push through the flow of the artifactz.io service
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushVersionRequest extends Request {
    private static final long serialVersionUID = 1719249389867475917L;

    /**
     * Stage name
     */
    @JsonProperty("stage_name")
    private String stage;

    /**
     * Artifact name
     */
    @JsonProperty("artifact_name")
    private String name;

    /**
     * Artifact version to push
     */
    @JsonProperty("version")
    private String version;

    /**
     * Default constructor
     */
    public PushVersionRequest() {

    }

    /**
     * Override constructor allowing to set all message properties at once
     *
     * @param stage stage from where artifact should be pushed
     * @param name name of the artifact to push
     * @param version version of the artifact to push
     */
    @JsonCreator
    public PushVersionRequest(@JsonProperty(value = "stage_name", required = true) String stage,
                              @JsonProperty(value = "artifact_name", required = true) String name,
                              @JsonProperty("version") String version) {
        this.stage = stage;
        this.name = name;
        this.version = version;
    }

    /**
     * Returns the current stage
     *
     * @return current stage
     */
    @JsonGetter("stage_name")
    public String getStage() {
        return stage;
    }

    /**
     * Sets the current stage
     *
     * @param stage current stage
     */
    public void setStage(String stage) {
        this.stage = stage;
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
     * Sets the artifact name
     *
     * @param name artifact name
     */
    public void setName(String name) {
        this.name = name;
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
     * Sets the artifact version
     *
     * @param version artifact version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
