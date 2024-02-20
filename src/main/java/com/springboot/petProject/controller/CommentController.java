package com.springboot.petProject.controller;

import com.springboot.petProject.dto.CommentDto;
import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.CommentRequest;
import com.springboot.petProject.dto.response.CommentResponse;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.service.AuthenticationService;
import com.springboot.petProject.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthenticationService authenticationService;

    @GetMapping("/{postId}/comment")
    public Response<List<CommentResponse>> getComments(@PathVariable Integer postId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        List<CommentDto> comment = commentService.getComments(postId, user.getUserId());
        return Response.success(comment.stream()
                .map(CommentResponse::fromDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{postId}/comment")
    public Response<Void> create(@PathVariable Integer postId, @Valid @RequestBody CommentRequest request, Authentication authentication) {
        commentService.create(postId, request.getComment(), authentication.getName());
        return Response.success();
    }

    @PatchMapping("/{postId}/comment/{commentId}")
    public Response<CommentResponse> update(@PathVariable Integer commentId, @Valid @RequestBody CommentRequest request, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        CommentDto comment = commentService.update(commentId, request.getComment(), user.getUserId());
        return Response.success(CommentResponse.fromDto(comment));
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public Response<Void> delete(@PathVariable Integer commentId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        commentService.delete(commentId, user.getUserId());
        return Response.success();
    }

}
