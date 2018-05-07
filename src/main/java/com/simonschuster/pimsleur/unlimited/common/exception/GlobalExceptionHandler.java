package com.simonschuster.pimsleur.unlimited.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.simonschuster.pimsleur.unlimited.common.exception.ErrorCode.UNKNOWN_ERROR;
import static com.simonschuster.pimsleur.unlimited.common.exception.ErrorResponse.of;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler()
    @ResponseBody
    public ResponseEntity<?> handleException(Exception ex) {
        HttpStatus httpStatus = resolveAnnotatedResponseStatus(ex);
        String message = ex.getMessage();
        if (message == null) {
            message = "系统错误.";
        }

        int errorCode = ex instanceof PimsleurException ? ((PimsleurException) ex).getErrorCode() : UNKNOWN_ERROR;
        return new ResponseEntity<>(of(httpStatus.value(), errorCode, message), new HttpHeaders(), httpStatus);
    }

    private HttpStatus resolveAnnotatedResponseStatus(Exception exception) {

        ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);

        if (annotation != null) {
            return annotation.value();
        }

        return INTERNAL_SERVER_ERROR;
    }
}
