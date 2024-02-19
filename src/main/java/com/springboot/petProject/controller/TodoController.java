package com.springboot.petProject.controller;

import com.springboot.petProject.dto.TodoDto;
import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.TodoCreateRequest;
import com.springboot.petProject.dto.request.TodoUpdateRequest;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.dto.response.TodoResponse;
import com.springboot.petProject.service.AuthenticationService;
import com.springboot.petProject.service.TodoService;
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

    @GetMapping("/{todoId}")
    public Response<TodoResponse> getTodo(@PathVariable Integer todoId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        TodoDto todo = todoService.getTodo(todoId, user.getUserId());
        return Response.success(TodoResponse.fromDto(todo));
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
