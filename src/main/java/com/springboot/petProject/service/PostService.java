package com.springboot.petProject.service;

import com.springboot.petProject.dto.DetailPostDto;
import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.repository.CommentRepository;
import com.springboot.petProject.repository.PostRepository;
import com.springboot.petProject.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ExceptionService exceptionService;
    private final Validate validate;

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PostDto> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostDto::fromEntity);
    }

    public DetailPostDto getPost(Integer postId) {
        Post post = exceptionService.getPostOrThrowException(postId);
        return DetailPostDto.fromEntity(post);
    }

    @Transactional
    public void create(String title, String contents, String username) {
        validateTitleAndContentsNotNull(title, contents);
        validate.validateBadWord(title);
        validate.validateBadWord(contents);
        User user = exceptionService.getUserOrThrowException(username);
        postRepository.save(Post.of(title, contents, user));
    }

    @Transactional
    public DetailPostDto update(Integer postId, String title, String contents, Integer userId) {
        validateTitleAndContentsNotNull(title, contents);
        validate.validateBadWord(title);
        validate.validateBadWord(contents);
        Post post = exceptionService.getPostIfAuthorized(postId, userId);

        post.setTitle(title);
        post.setContents(contents);

        return DetailPostDto.fromEntity(postRepository.save(post));
    }

    private void validateTitleAndContentsNotNull(String title, String contents) {
        if (title == null || contents == null) {
            throw new CustomExceptionHandler(ErrorCode.HTTP_MESSAGE_NOT_READABLE);
        }
    }

    @Transactional
    public void deletePost(Integer postId, Integer userId) {
        Post post = exceptionService.getPostIfAuthorized(postId, userId);
        commentRepository.deleteAllByPost(post);
        postRepository.deleteByPost(post);
    }
}
