package com.springboot.petProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "`post_view_log`")
@Setter
@Getter
@SQLDelete(sql = "UPDATE `post_view_log` SET removed_at = NOW() WHERE post_view_log_id=?")
@SQLRestriction("removed_at is NULL")
public class PostViewLog {

    @Id
    @GeneratedValue
    @Column(name = "post_view_log_id")
    private Integer id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "viewed_at")
    private Timestamp viewedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @PrePersist
    void createdAt() {
        this.viewedAt = Timestamp.from(Instant.now());
    }

    public static PostViewLog create(String ipAddress, Post post) {
        PostViewLog entity = new PostViewLog();
        entity.setIpAddress(ipAddress);
        entity.setPost(post);
        return entity;
    }

}
