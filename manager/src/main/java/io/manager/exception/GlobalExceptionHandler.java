package io.manager.exception;

import io.manager.dto.DetailResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NoWorkersAvailableException.class})
    public ResponseEntity<DetailResponse> noWorkersAvailableException(Exception ex, WebRequest request) {
        log.warn("503 error, no available workers {}", ex.getMessage());
        DetailResponse detailResponse = new DetailResponse(ex.getMessage());
        return new ResponseEntity<>(detailResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = {RequestNotFoundException.class})
    public ResponseEntity<DetailResponse> requestNotFoundException(Exception ex, WebRequest request) {
        log.warn("404 error, request not found {}", ex.getMessage());
        DetailResponse detailResponse = new DetailResponse(ex.getMessage());
        return new ResponseEntity<>(detailResponse, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(value = {ConstraintViolationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, MethodValidationException.class})
//    public ResponseEntity<DetailResponse> validationException(Exception ex, WebRequest request) {
//        log.warn("422 error, validation error {}", ex.getMessage());
//        DetailResponse detailResponse = new DetailResponse(ex.getMessage());
//        return new ResponseEntity<>(detailResponse, HttpStatus.UNPROCESSABLE_ENTITY);
//    }

}
