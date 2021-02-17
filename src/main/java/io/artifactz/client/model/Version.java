package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * The JSON message representing a single artifact version in the response from the get artifacts for stage call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Version implements Serializable {
    private static final long serialVersionUID = -4840017712293958874L;

    /**
     * Artifact name
     */
    private String artifactName;

    /**
     * Artifact description
     */
    private String artifactDescription;

    /**
     * Artifact type
     */
    private String type;

    /**
     * Maven group Id
     */
    private String groupId;

    /**
     * Maven artifact Id
     */
    private String artifactId;

    /**
     * Artifact version
     */
    private String version;

    /**
     * Default constructor
     */
    public Version() {

    }

    /**
     * Override constructor
     *
     * @param artifactName artifact name
     * @param artifactDescription artifact description
     * @param type artifact type
     * @param groupId maven group Id
     * @param artifactId maven artifact Id
     * @param version artifact version
     */
    public Version(@JsonProperty(value = "artifact_name", required = true) String artifactName,
                   @JsonProperty("artifact_description") String artifactDescription,
                   @JsonProperty(value = "type", required = true) String type,
                   @JsonProperty("group_id") String groupId,
                   @JsonProperty("artifact_id") String artifactId,
                   @JsonProperty(value = "version", required = false) String version) {
        this.artifactName = artifactName;
        this.artifactDescription = artifactDescription;
        this.type = type;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * Returns the artifact name
     *
     * @return artifact name
     */
    @JsonGetter("artifact_name")
    public String getArtifactName() {
        return artifactName;
    }

    /**
     * Sets the artifact name
     *
     * @param artifactName artifact name
     */
    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    /**
     * Returns the artifact description
     *
     * @return artifact description
     */
    @JsonGetter("artifact_description")
    public String getArtifactDescription() {
        return artifactDescription;
    }

    /**
     * Sets the artifact description
     *
     * @param artifactDescription artifact description
     */
    public void setArtifactDescription(String artifactDescription) {
        this.artifactDescription = artifactDescription;
    }

    /**
     * Returns the artifact type
     *
     * @return artifact type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the artifact type
     *
     * @param type artifact type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns maven group Id
     *
     * @return maven group Id
     */
    @JsonGetter("group_id")
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets maven group Id
     *
     * @param groupId maven group Id
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * Returns maven artifact Id
     *
     * @return maven artifact Id
     */
    @JsonGetter("artifact_id")
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Sets maven artifact Id
     *
     * @param artifactId maven artifact Id
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Returns artifact version
     *
     * @return artifact version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets artifact version
     *
     * @param version artifact version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
