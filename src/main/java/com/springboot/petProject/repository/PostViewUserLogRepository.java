package com.springboot.petProject.repository;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.PostViewUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PostViewUserLogRepository extends JpaRepository<PostViewUserLog, Integer> {

    Optional<PostViewUserLog> findByPostIdAndUserId(Integer postId, Integer userId);

    @Transactional
    @Modifying
    @Query("UPDATE PostViewUserLog postViewUserLog SET postViewUserLog.removedAt = CURRENT_TIMESTAMP WHERE postViewUserLog.post = :post")
    void deleteAllByPost(@Param("post") Post post);

}
