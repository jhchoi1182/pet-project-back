package com.springboot.petProject.controller;

import com.springboot.petProject.dto.CommentDto;
import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.CommentRequest;
import com.springboot.petProject.dto.response.CommentResponse;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.service.AuthenticationService;
import com.springboot.petProject.service.CommentService;
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
    private final AuthenticationService authenticationService;

    @GetMapping("/{todoId}/comment")
    public Response<List<CommentResponse>> getComments(@PathVariable Integer todoId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        List<CommentDto> comment = commentService.getComments(todoId, user.getUserId());
        return Response.success(comment.stream()
                .map(CommentResponse::fromDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{todoId}/comment")
    public Response<Void> create(@PathVariable Integer todoId, @RequestBody CommentRequest request, Authentication authentication) {
        commentService.create(todoId, request.getComment(), authentication.getName());
        return Response.success();
    }

    @PatchMapping("/{todoId}/comment/{commentId}")
    public Response<CommentResponse> update(@PathVariable Integer commentId, @RequestBody CommentRequest request, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        CommentDto comment = commentService.update(commentId, request.getComment(), user.getUserId());
        return Response.success(CommentResponse.fromDto(comment));
    }

    @DeleteMapping("/{todoId}/comment/{commentId}")
    public Response<Void> delete(@PathVariable Integer commentId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        commentService.delete(commentId, user.getUserId());
        return Response.success();
    }


}
