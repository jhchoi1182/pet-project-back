package com.springboot.petProject.repository;

import com.springboot.petProject.entity.Comment;
import com.springboot.petProject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT comment FROM Comment comment " +
            "LEFT JOIN FETCH comment.post post " +
            "LEFT JOIN FETCH post.user " +
            "WHERE comment.post.id = :postId AND comment.removedAt IS NULL AND post.removedAt IS NULL")
    List<Comment> findAllByPostIdWithUser(@Param("postId") Integer postId);

    @Transactional
    @Modifying
    @Query("UPDATE Comment comment SET comment.removedAt = CURRENT_TIMESTAMP WHERE comment.post = :post")
    void deleteAllByPost(@Param("post") Post post);

}
