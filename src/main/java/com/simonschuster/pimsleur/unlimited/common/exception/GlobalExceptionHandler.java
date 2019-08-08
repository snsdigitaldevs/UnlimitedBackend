package com.simonschuster.pimsleur.unlimited.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import static com.simonschuster.pimsleur.unlimited.common.exception.ErrorResponse.of;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler()
  public ResponseEntity<?> handleException(Exception ex) {
    HttpStatus httpStatus = resolveAnnotatedResponseStatus(ex);
    String message = ex.getMessage();
    if (message == null) {
      message = "System error";
    }
    recordLog(ex);
    return parseResponseEntity(of(httpStatus.value(), message), httpStatus);
  }

  @ExceptionHandler({RestClientException.class})
  public ResponseEntity<?> handleEdtRequestException(Exception e) {
    recordLog(e);
    return parseResponseEntity(
        of(INTERNAL_SERVER_ERROR.value(), e.getMessage()),
        INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({EdtResponseException.class})
  public ResponseEntity<?> handleEdtResponseException(Exception e) {
    return parseResponseEntity(of(INTERNAL_SERVER_ERROR.value(), e.getMessage()),
        INTERNAL_SERVER_ERROR);

  }

  @ExceptionHandler({MissingServletRequestParameterException.class, ParamInvalidException.class})
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Exception e) {
    String errorMessage = e.getMessage();
    ErrorResponse response = of(BAD_REQUEST.value(), errorMessage);
    recordLog(e);
    return new ResponseEntity<>(response, new HttpHeaders(), BAD_REQUEST);
  }

  private HttpStatus resolveAnnotatedResponseStatus(Exception exception) {

    ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);

    if (annotation != null) {
      return annotation.value();
    }

    return INTERNAL_SERVER_ERROR;
  }

  private void recordLog(Exception e) {
    LOG.error(e.getMessage(), e);
  }

  private ResponseEntity<?> parseResponseEntity(ErrorResponse response, HttpStatus httpStatus) {
    return new ResponseEntity<>(response, new HttpHeaders(), httpStatus);
  }
}
