package com.springboot.petProject.exception;

import com.springboot.petProject.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("error occurs {}", e.toString());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(Response.error(ErrorCode.URL_NOT_FOUND.name()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("error occurs: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(Response.error(ErrorCode.METHOD_NOT_ALLOWED.name()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("error occurs: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.error(ErrorCode.HTTP_MESSAGE_NOT_READABLE.name()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.error("error occurs {}", e.toString());
        String errorMessage = getJoinedErrorMessage(e);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(Response.error(errorMessage));
    }

    private String getJoinedErrorMessage(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> {
                    if (objectError instanceof FieldError) {
                        return ((FieldError) objectError).getDefaultMessage();
                    } else {
                        return objectError.getDefaultMessage();
                    }
                })
                .collect(Collectors.toList());

        return String.join(", ", errorMessages);
    }

    @ExceptionHandler(CustomExceptionHandler.class)
    public ResponseEntity<?> handleCustomException(CustomExceptionHandler e) {
        log.error("error occurs {}", e.toString());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        log.error("error occurs {}", e.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.DATABASE_ERROR.name()));
    }

}
