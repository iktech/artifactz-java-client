package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushVersionRequest extends Request {
    private static final long serialVersionUID = 1719249389867475917L;
    @JsonProperty("stage_name")
    private String stage;
    @JsonProperty("artifact_name")
    private String name;
    @JsonProperty("version")
    private String version;

    public PushVersionRequest() {

    }

    @JsonCreator
    public PushVersionRequest(@JsonProperty(value = "stage_name", required = true) String stage,
                              @JsonProperty(value = "artifact_name", required = true) String name,
                              @JsonProperty("version") String version) {
        this.stage = stage;
        this.name = name;
        this.version = version;
    }

    @JsonGetter("stage_name")
    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @JsonGetter("artifact_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
