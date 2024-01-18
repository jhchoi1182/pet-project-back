package com.springboot.todo.service;

import com.springboot.todo.entity.Todo;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ErrorCode;
import com.springboot.todo.exception.TodoExceptionHandler;
import com.springboot.todo.repository.TodoRepository;
import com.springboot.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

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

    public Todo getTodoIfAuthorized(Integer todoId, String username) {
        User user = getUserOrThrowException(username);
        Todo todo = getTodoOrThrowException(todoId);

        if(todo.getUser() != user) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with todo %d", username, todoId));
        }
        return todo;
    }



}
