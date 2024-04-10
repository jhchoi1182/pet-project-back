package com.springboot.petProject.repository;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.PostLikeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PostLikeUserRepository extends JpaRepository<PostLikeUser, Integer>, JpaSpecificationExecutor<Post> {

    Optional<PostLikeUser> findByPostIdAndUserId(Integer postId, Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PostLikeUser postLikeUser WHERE postLikeUser.post = :post")
    void deleteAllByPost(@Param("post") Post post);

}
