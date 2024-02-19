package com.springboot.petProject.service;

import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.repository.CommentRepository;
import com.springboot.petProject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;

    public List<PostDto> getPosts() {
        return postRepository.findAll().stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PostDto getPost(Integer postId, Integer userId) {
        Post post = authenticationService.getPostIfAuthorized(postId, userId);
        return PostDto.fromEntity(post);
    }

    @Transactional
    public void create(String title, String contents, String username) {
        User user = authenticationService.getUserOrThrowException(username);
        postRepository.save(Post.of(title, contents, user));
    }

    @Transactional
    public PostDto updateContents(Integer postId, String title, String contents, Integer userId) {
        Post post = authenticationService.getPostIfAuthorized(postId, userId);

        post.setTitle(title);
        post.setContents(contents);

        return PostDto.fromEntity(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Integer postId, Integer userId) {
        Post post = authenticationService.getPostIfAuthorized(postId, userId);
        commentRepository.deleteAllByPost(post);
        postRepository.delete(post);
    }

}
