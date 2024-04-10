package com.springboot.petProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "`post_like_user`")
@Setter
@Getter
public class PostLikeUser {

    @Id
    @GeneratedValue
    @Column(name = "post_like_user_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static PostLikeUser add(Post post, User user) {
        PostLikeUser entity = new PostLikeUser();
        entity.setPost(post);
        entity.setUser(user);
        return entity;
    }
}
