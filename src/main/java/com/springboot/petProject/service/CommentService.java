package com.springboot.petProject.service;

import com.springboot.petProject.dto.CommentDto;
import com.springboot.petProject.entity.Comment;
import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AuthenticationService authenticationService;
    private final CommentRepository commentRepository;

    public List<CommentDto> getComments(Integer postId, Integer userId) {
        Post post = authenticationService.getPostIfAuthorized(postId, userId);
        return commentRepository.findAllByPostId(post.getId()).stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(Integer postId, String comment, String username) {
        Post post = authenticationService.getPostOrThrowException(postId);
        User user = authenticationService.getUserOrThrowException(username);
        authenticationService.validatePermission(post, user);

        commentRepository.save(Comment.of(user, post, comment));

    }

    @Transactional
    public CommentDto update(Integer commentId, String comment, Integer userId) {
        Comment commentEntity = getCommentIfAuthorized(commentId, userId);

        commentEntity.setComment(comment);

        return CommentDto.fromEntity(commentRepository.save(commentEntity));
    }

    @Transactional
    public void delete(Integer commentId, Integer userId) {
        Comment comment = getCommentIfAuthorized(commentId, userId);
        commentRepository.delete(comment);
    }

    private Comment getCommentIfAuthorized(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomExceptionHandler(ErrorCode.COMMENT_NOT_FOUND, String.format("%s is not found", commentId)));

        if(!Objects.equals(comment.getUser().getId(), userId)) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with comment %d", userId, commentId));
        }
        return comment;
    }

}
