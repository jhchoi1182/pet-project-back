package com.springboot.petProject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "Invalid request body"),
    PASSWORDS_NOT_MATCHING(HttpStatus.BAD_REQUEST, "Passwords do not match"),
    INVALID_REQUEST_VALUE(HttpStatus.BAD_REQUEST, "Invalid request value"),
    INVALID_INFO(HttpStatus.BAD_REQUEST, "Invalid user information"),
    PROFANITY_INCLUDED(HttpStatus.BAD_REQUEST, "Value contains profanity"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "User has invalid permission"),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "The requested URL was not found on the server"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User is not found"),
    USER_REMOVED(HttpStatus.NOT_FOUND, "This user has been removed"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post is not found"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment is not found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Invalid request Method"),
    DUPLICATED_NAME(HttpStatus.CONFLICT, "Duplicated name"),
    SERVER_ERROR(HttpStatus.CONFLICT, "Server error occurs"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurs"),
    CLASS_CAST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Casting error occurred"),
    AUTHENTICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Authentication")
    ;

    private final HttpStatus status;
    private final String message;
}
