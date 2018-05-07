package com.simonschuster.pimsleur.unlimited.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PimsleurException extends RuntimeException {
    private final int errorCode;

    @JsonCreator
    public PimsleurException(@JsonProperty("errorCode") int errorCode,
                             @JsonProperty("message") String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PimsleurException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
