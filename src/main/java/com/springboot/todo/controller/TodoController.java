package com.springboot.todo.controller;

import com.springboot.todo.dto.request.TodoCreateRequest;
import com.springboot.todo.dto.response.Response;
import com.springboot.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
