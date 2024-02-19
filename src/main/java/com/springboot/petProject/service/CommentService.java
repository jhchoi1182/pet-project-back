package com.springboot.petProject.service;

import com.springboot.petProject.dto.CommentDto;
import com.springboot.petProject.entity.Comment;
import com.springboot.petProject.entity.Todo;
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

    public List<CommentDto> getComments(Integer todoId, Integer userId) {
        Todo todo = authenticationService.getTodoIfAuthorized(todoId, userId);
        return commentRepository.findAllByTodoId(todo.getId()).stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(Integer todoId, String comment, String username) {
        Todo todo = authenticationService.getTodoOrThrowException(todoId);
        User user = authenticationService.getUserOrThrowException(username);
        authenticationService.validatePermission(todo, user);

        commentRepository.save(Comment.of(user, todo, comment));

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
