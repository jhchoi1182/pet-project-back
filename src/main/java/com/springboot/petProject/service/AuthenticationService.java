package com.springboot.petProject.service;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.entity.Todo;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.repository.TodoRepository;
import com.springboot.petProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    public UserDto getAuthenticationPrincipal(Authentication authentication) {
        try {
            return (UserDto) authentication.getPrincipal();
        } catch (ClassCastException e) {
            throw new CustomExceptionHandler(ErrorCode.CLASS_CAST_ERROR, "Casting to UserDto class failed");
        }
    }

    public User getUserOrThrowException(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", username)));
    }

    public Todo getTodoOrThrowException(Integer todoId) {
        return todoRepository.findById(todoId).orElseThrow(() ->
                new CustomExceptionHandler(ErrorCode.TODO_NOT_FOUND, String.format("%s is not found", todoId)));
    }

    public void validatePermission(Todo todo, User user) {
        if(todo.getUser() != user) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with todo %d", user.getUsername(), todo.getId()));
        }
    }

    public Todo getTodoIfAuthorized(Integer todoId, Integer userId) {
        Todo todo = getTodoOrThrowException(todoId);

        if(!Objects.equals(todo.getUser().getId(), userId)) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with todo %d", userId, todoId));
        }
        return todo;
    }



}
