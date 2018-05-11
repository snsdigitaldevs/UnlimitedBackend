package com.simonschuster.pimsleur.unlimited.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.simonschuster.pimsleur.unlimited.common.exception.ErrorResponse.of;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler()
    @ResponseBody
    public ResponseEntity<?> handleException(Exception ex) {
        HttpStatus httpStatus = resolveAnnotatedResponseStatus(ex);
        String message = ex.getMessage();
        if (message == null) {
            message = "System error";
        }
        printLog(message, ex);
        return new ResponseEntity<>(of(httpStatus.value(), message), new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, ParamInvalidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Exception e) {
        String errorMessage = e.getMessage();
        ErrorResponse response = of(BAD_REQUEST.value(), errorMessage);
        printLog(errorMessage, e);
        return new ResponseEntity<>(response, new HttpHeaders(), BAD_REQUEST);
    }

    private HttpStatus resolveAnnotatedResponseStatus(Exception exception) {

        ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);

        if (annotation != null) {
            return annotation.value();
        }

        return INTERNAL_SERVER_ERROR;
    }

    private void printLog(String message, Exception ex) {
        logger.error(message, ex);
    }
}
