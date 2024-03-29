package com.springboot.petProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "`comment`")
@Setter
@Getter
@SQLDelete(sql = "UPDATE `comment` SET removed_at = NOW() WHERE comment_id=?")
@SQLRestriction("removed_at is NULL")
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Integer id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static Comment of(User user, Post post, String comment) {
        Comment entity = new Comment();
        entity.setUser(user);
        entity.setPost(post);
        entity.setComment(comment);
        return entity;
    }

}
