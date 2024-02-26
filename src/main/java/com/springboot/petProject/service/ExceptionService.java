package com.springboot.petProject.service;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.repository.PostRepository;
import com.springboot.petProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExceptionService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserDto getAuthenticationPrincipal(Authentication authentication) {
        try {
            return (UserDto) authentication.getPrincipal();
        } catch (ClassCastException e) {
            throw new CustomExceptionHandler(ErrorCode.CLASS_CAST_ERROR, "Casting to UserDto class failed");
        }
    }

    public User getUserOrThrowException(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", username)));
    }

    public Post getPostOrThrowException(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new CustomExceptionHandler(ErrorCode.POST_NOT_FOUND, String.format("%s is not found", postId)));
    }

    public void validatePermission(Post post, User user) {
        if(post.getUser() != user) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with post %d", user.getUsername(), post.getId()));
        }
    }

    public Post getPostIfAuthorized(Integer postId, Integer userId) {
        Post post = getPostOrThrowException(postId);

        if(!Objects.equals(post.getUser().getId(), userId)) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with post %d", userId, postId));
        }
        return post;
    }



}
