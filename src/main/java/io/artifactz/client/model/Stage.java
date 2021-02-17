package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * The JSON message representing response from the get artifacts for stage call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Stage implements Serializable {
    private static final long serialVersionUID = 5665727389410186012L;

    /**
     * Stage name
     */
    private String stage;

    /**
     * List of the artifacts at the stage
     */
    private List<Version> artifacts;

    /**
     * Default constructor
     */
    public Stage() {

    }

    /**
     * Override constructor
     *
     * @param stage the stage name
     * @param artifacts the list of the request artifacts at the above stage
     */
    public Stage(@JsonProperty(value = "stage", required = true) String stage,
                 @JsonProperty(value = "artifacts", required = true) List<Version> artifacts) {
        this.stage = stage;
        this.artifacts = artifacts;
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
     * Sets the stage name
     *
     * @param stage stage name
     */
    public void setStage(String stage) {
        this.stage = stage;
    }

    /**
     * Returns the list of the requested artifacts at the specified stage
     *
     * @return list of the artifacts at the stage
     */
    public List<Version> getArtifacts() {
        return artifacts;
    }

    /**
     * Sets the list of the artifacts
     *
     * @param artifacts list of the artifacts
     */
    public void setArtifacts(List<Version> artifacts) {
        this.artifacts = artifacts;
    }
}
