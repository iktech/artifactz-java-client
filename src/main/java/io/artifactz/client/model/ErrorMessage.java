package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = -4073983053043758408L;
    @JsonProperty("error")
    private String error;

    public ErrorMessage() {

    }

    @JsonCreator
    public ErrorMessage(@JsonProperty(value = "error", required = true) String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
