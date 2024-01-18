package com.springboot.todo.controller;

import com.springboot.todo.dto.TodoDto;
import com.springboot.todo.dto.request.TodoCreateRequest;
import com.springboot.todo.dto.request.TodoUpdateRequest;
import com.springboot.todo.dto.response.Response;
import com.springboot.todo.dto.response.TodoResponse;
import com.springboot.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public Response<Void> create(@RequestBody TodoCreateRequest request, Authentication authentication) {
        todoService.create(request.getContents(), request.getDueDate(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{todoId}")
    public Response<TodoResponse> update(@PathVariable Integer todoId, @RequestBody TodoUpdateRequest request, Authentication authentication) {
        TodoDto todo = todoService.updateContents(todoId, request.getContents(), request.getDueDate(), authentication.getName());
        return Response.success(TodoResponse.fromDto(todo));
    }

    @PatchMapping("/{todoId}")
    public Response<TodoResponse> toggleIsDone(@PathVariable Integer todoId, Authentication authentication) {
        TodoDto todo = todoService.updateIsDone(todoId, authentication.getName());
        return Response.success(TodoResponse.fromDto(todo));
    }

}
