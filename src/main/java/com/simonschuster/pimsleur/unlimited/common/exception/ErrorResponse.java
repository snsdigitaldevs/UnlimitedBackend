package com.simonschuster.pimsleur.unlimited.common.exception;

public class ErrorResponse {

    private int httpStatusCode;
    private int errorCode;
    private String errorMessage;

    private ErrorResponse() {
    }

    private ErrorResponse(int httpStatusCode, int errorCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse of(int httpStatusCode, int errorCode, String errorMessage) {
        return new ErrorResponse(httpStatusCode, errorCode, errorMessage);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
