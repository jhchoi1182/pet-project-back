package com.springboot.todo.controller;

import com.springboot.todo.dto.CommentDto;
import com.springboot.todo.dto.request.CommentRequest;
import com.springboot.todo.dto.response.CommentResponse;
import com.springboot.todo.dto.response.Response;
import com.springboot.todo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{todoId}/comment")
    public Response<List<CommentResponse>> getList(@PathVariable Integer todoId, Authentication authentication) {
        List<CommentDto> comment = commentService.getComments(todoId);
        return Response.success(comment.stream()
                .map(CommentResponse::fromDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{todoId}/comment")
    public Response<Void> comment(@PathVariable Integer todoId, @RequestBody CommentRequest request, Authentication authentication) {
        commentService.create(todoId, request.getComment(), authentication.getName());
        return Response.success();

    }


}
