package io.artifactz.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * The JSON message that represents the server error
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = -4073983053043758408L;

    /**
     * Exception error message
     */
    @JsonProperty("error")
    private String error;

    /**
     * Default constructor
     */
    public ErrorMessage() {

    }

    /**
     * Override constructor that allows to set the error message
     * @param error error message
     */
    @JsonCreator
    public ErrorMessage(@JsonProperty(value = "error", required = true) String error) {
        this.error = error;
    }

    /**
     * Method returns the error message
     *
     * @return error message
     */
    public String getError() {
        return error;
    }

    /**
     * Standard setter for error message
     *
     * @param error new error message
     */
    public void setError(String error) {
        this.error = error;
    }
}
