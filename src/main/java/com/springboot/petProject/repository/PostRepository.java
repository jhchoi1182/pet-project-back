package com.springboot.petProject.repository;

import com.springboot.petProject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByUserId(Integer userId);

    @Transactional
    @Modifying
    @Query("UPDATE Post post SET post.removedAt = CURRENT_TIMESTAMP WHERE post = :post")
    void deleteByPost(@Param("post") Post post);

}
