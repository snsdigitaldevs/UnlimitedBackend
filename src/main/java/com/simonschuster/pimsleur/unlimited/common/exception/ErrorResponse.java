package com.simonschuster.pimsleur.unlimited.common.exception;

public class ErrorResponse {

    private int httpStatusCode;
    private String errorMessage;

    private ErrorResponse() {
    }

    private ErrorResponse(int httpStatusCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse of(int httpStatusCode, String errorMessage) {
        return new ErrorResponse(httpStatusCode, errorMessage);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
