package com.springboot.todo.service;

import com.springboot.todo.dto.CommentDto;
import com.springboot.todo.dto.TodoDto;
import com.springboot.todo.entity.Comment;
import com.springboot.todo.entity.Todo;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ErrorCode;
import com.springboot.todo.exception.TodoExceptionHandler;
import com.springboot.todo.repository.CommentRepository;
import com.springboot.todo.repository.TodoRepository;
import com.springboot.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommonService commonService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public void create(Integer todoId, String comment, String username) {

        Todo todo = commonService.getTodoOrThrowException(todoId);
        User user = commonService.getUserOrThrowException(username);

        commentRepository.save(Comment.of(user, todo, comment));

    }

    public List<CommentDto> getComments(Integer todoId) {

        Todo todo = commonService.getTodoOrThrowException(todoId);

        return commentRepository.findAllByTodoId(todo.getId()).stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());

    }
}
