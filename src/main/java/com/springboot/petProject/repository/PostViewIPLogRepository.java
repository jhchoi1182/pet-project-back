package com.springboot.petProject.repository;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.PostViewIPLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PostViewIPLogRepository extends JpaRepository<PostViewIPLog, Integer> {

    Optional<PostViewIPLog> findByPostIdAndIpAddress(Integer postId, String ipAddress);

    @Transactional
    @Modifying
    @Query("UPDATE PostViewIPLog postViewIPLog SET postViewIPLog.removedAt = CURRENT_TIMESTAMP WHERE postViewIPLog.post = :post")
    void deleteAllByPost(@Param("post") Post post);

}
