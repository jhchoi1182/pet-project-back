package com.springboot.todo.controller;

import com.springboot.todo.dto.TodoDto;
import com.springboot.todo.dto.UserDto;
import com.springboot.todo.dto.request.TodoCreateRequest;
import com.springboot.todo.dto.request.TodoUpdateRequest;
import com.springboot.todo.dto.response.Response;
import com.springboot.todo.dto.response.TodoResponse;
import com.springboot.todo.service.AuthenticationService;
import com.springboot.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public Response<List<TodoResponse>> getTodos(Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        List<TodoDto> todos = todoService.getTodos(user.getUserId());
        return Response.success(todos.stream()
                .map(TodoResponse::fromDto)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public Response<Void> create(@RequestBody TodoCreateRequest request, Authentication authentication) {
        todoService.create(request.getContents(), request.getDueDate(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{todoId}")
    public Response<TodoResponse> updateContents(@PathVariable Integer todoId, @RequestBody TodoUpdateRequest request, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        TodoDto todo = todoService.updateContents(todoId, request.getContents(), request.getDueDate(), user.getUserId());
        return Response.success(TodoResponse.fromDto(todo));
    }

    @PatchMapping("/{todoId}")
    public Response<TodoResponse> toggleIsDone(@PathVariable Integer todoId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        TodoDto todo = todoService.toggleIsDone(todoId, user.getUserId());
        return Response.success(TodoResponse.fromDto(todo));
    }

    @DeleteMapping("/{todoId}")
    public Response<Void> delete(@PathVariable Integer todoId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        todoService.deleteTodo(todoId, user.getUserId());
        return Response.success();
    }

}
