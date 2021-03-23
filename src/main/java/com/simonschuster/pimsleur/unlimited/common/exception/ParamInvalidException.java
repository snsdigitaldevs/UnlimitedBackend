package com.simonschuster.pimsleur.unlimited.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ParamInvalidException extends RuntimeException {

    @JsonCreator
    public ParamInvalidException(@JsonProperty("message") String message) {
        super(message);
    }

    public ParamInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
