package com.springboot.todo.service;

import com.springboot.todo.dto.CommentDto;
import com.springboot.todo.entity.Comment;
import com.springboot.todo.entity.Todo;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ErrorCode;
import com.springboot.todo.exception.TodoExceptionHandler;
import com.springboot.todo.repository.CommentRepository;
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

    public List<CommentDto> getComments(Integer todoId, String username) {

        Todo todo = authenticationService.getTodoIfAuthorized(todoId, username);

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
    public CommentDto update(Integer commentId, String comment, String username) {
        Comment commentEntity = getCommentIfAuthorized(commentId, username);

        commentEntity.setComment(comment);

        return CommentDto.fromEntity(commentRepository.save(commentEntity));
    }

    @Transactional
    public void delete(Integer commentId, String username) {
        Comment comment = getCommentIfAuthorized(commentId, username);
        commentRepository.delete(comment);
    }

    private Comment getCommentIfAuthorized(Integer commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.COMMENT_NOT_FOUND, String.format("%s is not found", commentId)));

        if(!Objects.equals(comment.getUser().getUsername(), username)) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with comment %d", username, commentId));
        }
        return comment;
    }

}