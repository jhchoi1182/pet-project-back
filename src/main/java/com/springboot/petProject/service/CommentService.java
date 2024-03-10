package com.springboot.petProject.service;

import com.springboot.petProject.dto.CommentDto;
import com.springboot.petProject.entity.Comment;
import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.repository.CommentRepository;
import com.springboot.petProject.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ExceptionService exceptionService;
    private final CommentRepository commentRepository;
    private final Validate validate;

    public List<CommentDto> getComments(Integer postId) {
        Post post = exceptionService.getPostOrThrowException(postId);
        return commentRepository.findAllByPostId(post.getId()).stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(Integer postId, String comment, String username) {
        validateCommentNotNull(comment);
        validate.validateBadWord(comment);
        Post post = exceptionService.getPostOrThrowException(postId);
        User user = exceptionService.getUserOrThrowException(username);

        commentRepository.save(Comment.of(user, post, comment));
    }

    @Transactional
    public CommentDto update(Integer commentId, String comment, Integer userId) {
        validateCommentNotNull(comment);
        validate.validateBadWord(comment);
        Comment commentEntity = getCommentIfAuthorized(commentId, userId);

        commentEntity.setComment(comment);

        return CommentDto.fromEntity(commentRepository.save(commentEntity));
    }

    private void validateCommentNotNull(String comment) {
        if (comment == null) {
            throw new CustomExceptionHandler(ErrorCode.HTTP_MESSAGE_NOT_READABLE);
        }
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
