package com.springboot.todo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PASSWORDS_NOT_MATCHING(HttpStatus.BAD_REQUEST, "Passwords do not match"),
    INVALID_REQUEST_VALUE(HttpStatus.BAD_REQUEST, "Invalid request value"),
    INVALID_INFO(HttpStatus.BAD_REQUEST, "Invalid user information"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "User has invalid permission"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User is not found"),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "Todo is not found"),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "Duplicated user name"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurs"),
    ;

    private final HttpStatus status;
    private final String message;
}
