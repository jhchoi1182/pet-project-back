package com.springboot.todo.repository;

import com.springboot.todo.entity.Comment;
import com.springboot.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByTodoId(Integer TodoId);

    @Transactional
    @Modifying
    @Query("UPDATE Comment comment SET comment.removedAt = CURRENT_TIMESTAMP WHERE comment.todo = :todo")
    void deleteAllByTodo(@Param("todo") Todo todo);

}
