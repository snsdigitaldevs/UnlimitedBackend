package com.simonschuster.pimsleur.unlimited.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PimsleurException extends RuntimeException {

    @JsonCreator
    public PimsleurException(@JsonProperty("message") String message) {
        super(message);
    }

    public PimsleurException(String message, Throwable cause) {
        super(message, cause);
    }
}
