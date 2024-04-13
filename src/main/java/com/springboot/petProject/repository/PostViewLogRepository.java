package com.springboot.petProject.repository;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.PostViewIPLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostViewLogRepository extends JpaRepository<PostViewIPLog, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE PostViewLog postViewLog SET postViewLog.removedAt = CURRENT_TIMESTAMP WHERE postViewLog.post = :post")
    void deleteAllByPost(@Param("post") Post post);

}
