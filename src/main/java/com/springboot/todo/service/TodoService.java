package com.springboot.todo.service;

import com.springboot.todo.dto.TodoDto;
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
import java.util.List;
import java.util.stream.Collectors;

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

    public List<TodoDto> getTodos(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND));

        return todoRepository.findAllByUserId(user.getId()).stream()
                .map(TodoDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoDto updateContents(Integer todoId, String contents, String dueDate, String username) {
        Todo todo = getTodoIfAuthorized(todoId, username);

        todo.setContents(contents);
        todo.setDueDate(dueDate);

        return TodoDto.fromEntity(todoRepository.save(todo));
    }

    @Transactional
    public TodoDto updateIsDone(Integer todoId, String username) {
        Todo todo = getTodoIfAuthorized(todoId, username);

        todo.setIsDone(!todo.getIsDone());

        return TodoDto.fromEntity(todoRepository.save(todo));
    }

    @Transactional
    public void deleteTodo(Integer todoId, String username) {
        Todo todo = getTodoIfAuthorized(todoId, username);

        todoRepository.delete(todo);
    }

    private Todo getTodoIfAuthorized(Integer todoId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", username)));

        Todo todo = todoRepository.findById(todoId).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.TODO_NOT_FOUND, String.format("%s is not found", todoId)));

        if(todo.getUser() != user) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with post %d", username, todoId));
        }
        return todo;
    }

}
