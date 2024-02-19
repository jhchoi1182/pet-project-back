package com.springboot.petProject.exception;

import com.springboot.petProject.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(CustomExceptionHandler.class)
    public ResponseEntity<?> applicationHandler(CustomExceptionHandler e) {
        log.error("error occurs {}", e.toString());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("error occurs {}", e.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.DATABASE_ERROR.name()));
    }

}
