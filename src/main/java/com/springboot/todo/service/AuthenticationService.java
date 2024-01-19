package com.springboot.todo.service;

import com.springboot.todo.dto.UserDto;
import com.springboot.todo.entity.Todo;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ErrorCode;
import com.springboot.todo.exception.TodoExceptionHandler;
import com.springboot.todo.repository.TodoRepository;
import com.springboot.todo.repository.UserRepository;
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
            throw new TodoExceptionHandler(ErrorCode.CLASS_CAST_ERROR, "Casting to UserDto class failed");
        }
    }

    public User getUserOrThrowException(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", username)));
    }

    public Todo getTodoOrThrowException(Integer todoId) {
        return todoRepository.findById(todoId).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.TODO_NOT_FOUND, String.format("%s is not found", todoId)));
    }

    public void validatePermission(Todo todo, User user) {
        if(todo.getUser() != user) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with todo %d", user.getUsername(), todo.getId()));
        }
    }

    public Todo getTodoIfAuthorized(Integer todoId, Integer userId) {
        Todo todo = getTodoOrThrowException(todoId);

        if(!Objects.equals(todo.getUser().getId(), userId)) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with todo %d", userId, todoId));
        }
        return todo;
    }



}
