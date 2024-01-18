package com.springboot.todo.service;

import com.springboot.todo.dto.TodoDto;
import com.springboot.todo.entity.Todo;
import com.springboot.todo.entity.User;
import com.springboot.todo.repository.CommentRepository;
import com.springboot.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;

    @Transactional
    public void create(String contents, String dueDate, String username) {
        User user = authenticationService.getUserOrThrowException(username);

        todoRepository.save(Todo.of(contents, dueDate, user));
    }

    public List<TodoDto> getTodos(String username) {
        User user = authenticationService.getUserOrThrowException(username);

        return todoRepository.findAllByUserId(user.getId()).stream()
                .map(TodoDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoDto updateContents(Integer todoId, String contents, String dueDate, String username) {
        Todo todo = authenticationService.getTodoIfAuthorized(todoId, username);

        todo.setContents(contents);
        todo.setDueDate(dueDate);

        return TodoDto.fromEntity(todoRepository.save(todo));
    }

    @Transactional
    public TodoDto updateIsDone(Integer todoId, String username) {
        Todo todo = authenticationService.getTodoIfAuthorized(todoId, username);

        todo.setIsDone(!todo.getIsDone());

        return TodoDto.fromEntity(todoRepository.save(todo));
    }

    @Transactional
    public void deleteTodo(Integer todoId, String username) {
        Todo todo = authenticationService.getTodoIfAuthorized(todoId, username);
        commentRepository.deleteAllByTodo(todo);
        todoRepository.delete(todo);
    }



}
