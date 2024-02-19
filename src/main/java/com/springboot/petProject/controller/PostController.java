package com.springboot.petProject.controller;

import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.PostCreateRequest;
import com.springboot.petProject.dto.request.PostUpdateRequest;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.dto.response.PostResponse;
import com.springboot.petProject.service.AuthenticationService;
import com.springboot.petProject.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public Response<List<PostResponse>> getPosts() {
        List<PostDto> posts = postService.getPosts();
        return Response.success(posts.stream()
                .map(PostResponse::fromDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{postId}")
    public Response<PostResponse> getPost(@PathVariable Integer postId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        PostDto post = postService.getPost(postId, user.getUserId());
        return Response.success(PostResponse.fromDto(post));
    }

    @PostMapping
    public Response<Void> create(@Valid @RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getContents(), authentication.getName());
        return Response.success();
    }

    @PatchMapping("/{postId}")
    public Response<PostResponse> updateContents(@PathVariable Integer postId, @Valid @RequestBody PostUpdateRequest request, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        PostDto post = postService.update(postId, request.getTitle(), request.getContents(), user.getUserId());
        return Response.success(PostResponse.fromDto(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        UserDto user = authenticationService.getAuthenticationPrincipal(authentication);
        postService.deletePost(postId, user.getUserId());
        return Response.success();
    }

}
