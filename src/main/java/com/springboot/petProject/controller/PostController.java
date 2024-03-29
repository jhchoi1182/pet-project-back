package com.springboot.petProject.controller;

import com.springboot.petProject.dto.DetailPostDto;
import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.post.PostCreateRequest;
import com.springboot.petProject.dto.request.post.PostUpdateRequest;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.dto.response.post.PostResponse;
import com.springboot.petProject.dto.response.post.PostsResponse;
import com.springboot.petProject.service.ExceptionService;
import com.springboot.petProject.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ExceptionService exceptionService;

    @GetMapping("/all")
    public Response<List<PostsResponse>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return Response.success(posts.stream()
                .map(PostsResponse::fromDto)
                .collect(Collectors.toList()));
    }

    @GetMapping
    public Response<Page<PostsResponse>> getPosts(Pageable pageable) {
        Page<PostDto> posts = postService.getPosts(pageable);
        return Response.success(posts.map(PostsResponse::fromDto));
    }

    @GetMapping("/{postId}")
    public Response<PostResponse> getPost(@PathVariable Integer postId) {
        DetailPostDto post = postService.getPost(postId);
        return Response.success(PostResponse.fromDto(post));
    }

    @PostMapping
    public Response<Void> create(@Valid @RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getContents(), request.getImages(), authentication.getName());
        return Response.success();
    }

    @PatchMapping("/{postId}")
    public Response<PostResponse> updateContents(@PathVariable Integer postId, @Valid @RequestBody PostUpdateRequest request, Authentication authentication) {
        UserDto user = exceptionService.getAuthenticationPrincipal(authentication);
        DetailPostDto post = postService.update(postId, request.getTitle(), request.getContents(), request.getImages(), user.getUserId());
        return Response.success(PostResponse.fromDto(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        UserDto user = exceptionService.getAuthenticationPrincipal(authentication);
        postService.deletePost(postId, user.getUserId());
        return Response.success();
    }

}
