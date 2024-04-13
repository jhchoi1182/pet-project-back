package com.springboot.petProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "`post_view_user_log`")
@Setter
@Getter
@SQLDelete(sql = "UPDATE `post_view_user_log` SET removed_at = NOW() WHERE post_view_user_log_id=?")
@SQLRestriction("removed_at is NULL")
public class PostViewUserLog {

    @Id
    @GeneratedValue
    @Column(name = "post_view_user_log_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "viewed_at")
    private Timestamp viewedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    @PrePersist
    void createdAt() {
        this.viewedAt = Timestamp.from(Instant.now());
    }

    public static PostViewUserLog add(Post post, User user) {
        PostViewUserLog entity = new PostViewUserLog();
        entity.setUser(user);
        entity.setPost(post);
        return entity;
    }
}
