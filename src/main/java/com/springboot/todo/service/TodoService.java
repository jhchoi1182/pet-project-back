package com.springboot.todo.service;

import com.springboot.todo.entity.Todo;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ErrorCode;
import com.springboot.todo.exception.TodoExceptionHandler;
import com.springboot.todo.repository.TodoRepository;
import com.springboot.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public void create(String contents, String dueDate, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)));

        todoRepository.save(Todo.of(contents, dueDate, user));
    }

}
