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

    public List<PostDto> getPosts(Integer userId) {
        return postRepository.findAllByUserId(userId).stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PostDto getPost(Integer postId, Integer userId) {
        Post post = authenticationService.getPostIfAuthorized(postId, userId);
        return PostDto.fromEntity(post);
    }

    @Transactional
    public void create(String contents, String dueDate, String username) {
        User user = authenticationService.getUserOrThrowException(username);
        postRepository.save(Post.of(contents, dueDate, user));
    }

    @Transactional
    public PostDto updateContents(Integer postId, String contents, String dueDate, Integer userId) {
        Post post = authenticationService.getPostIfAuthorized(postId, userId);

        post.setContents(contents);
        post.setDueDate(dueDate);

        return PostDto.fromEntity(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Integer postId, Integer userId) {
        Post post = authenticationService.getPostIfAuthorized(postId, userId);
        commentRepository.deleteAllByPost(post);
        postRepository.delete(post);
    }

}
