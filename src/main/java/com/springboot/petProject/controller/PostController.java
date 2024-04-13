package com.springboot.petProject.controller;

import com.springboot.petProject.dto.DetailPostDto;
import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.post.PostCreateRequest;
import com.springboot.petProject.dto.request.post.PostUpdateRequest;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.dto.response.post.PostResponse;
import com.springboot.petProject.dto.response.post.PostViewResponse;
import com.springboot.petProject.dto.response.post.PostsResponse;
import com.springboot.petProject.service.ExceptionService;
import com.springboot.petProject.service.post.PostService;
import com.springboot.petProject.service.user.CookieService;
import com.springboot.petProject.types.request.CategoryRequest;
import com.springboot.petProject.types.request.SearchType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final ExceptionService exceptionService;
    private final CookieService cookieService;

    @GetMapping("/initializeViewRecord")
    public Response<Void> setInitializeViewRecordCookie(HttpServletResponse response) {
        String encodedViewRecords = URLEncoder.encode(new JSONObject().toString(), StandardCharsets.UTF_8);
        cookieService.setHeaderViewRecordCookie(response, encodedViewRecords);
        return Response.success();
    }

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

    @GetMapping("/search")
    public Response<Page<PostsResponse>> searchPosts(@RequestParam CategoryRequest category, @RequestParam SearchType searchType, @RequestParam String value, Pageable pageable) {
        Page<PostDto> posts = postService.searchPosts(category, searchType, value, pageable);
        return Response.success(posts.map(PostsResponse::fromDto));
    }

    @GetMapping("/{postId}")
    public Response<PostResponse> getPost(@PathVariable Integer postId, Authentication authentication) {
        DetailPostDto post = postService.getPost(postId, authentication);
        return Response.success(PostResponse.fromDto(post));
    }

    @PatchMapping("/{postId}/views")
    public Response<PostViewResponse> updateViews(@PathVariable Integer postId, HttpServletRequest request, Authentication authentication) {
        String remoteAddr = request.getRemoteAddr();
        Integer views = postService.updateViews(postId, remoteAddr, authentication);
        return Response.success(new PostViewResponse(views));
    }

    @PostMapping
    public Response<Void> create(@Valid @RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getCategory(), request.getTitle(), request.getContents(), request.getImages(), authentication.getName());
        return Response.success();
    }

    @PatchMapping("/{postId}")
    public Response<PostResponse> updateContents(@PathVariable Integer postId, @Valid @RequestBody PostUpdateRequest request, Authentication authentication) {
        UserDto user = exceptionService.getAuthenticationPrincipal(authentication);
        DetailPostDto post = postService.update(postId, request.getCategory(), request.getTitle(), request.getContents(), request.getImages(), user.getUserId());
        return Response.success(PostResponse.fromDto(post));
    }

    @PatchMapping("/{postId}/toggle-like")
    public Response<Void> updatePostLike(@PathVariable Integer postId, Authentication authentication) {
        UserDto user = exceptionService.getAuthenticationPrincipal(authentication);
        postService.updatePostLike(postId, user.getUserId(), user.getUsername());
        return Response.success();
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        UserDto user = exceptionService.getAuthenticationPrincipal(authentication);
        postService.deletePost(postId, user.getUserId());
        return Response.success();
    }

}
