package com.springboot.petProject.entity;

import com.springboot.petProject.types.PostCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "`post`")
@Setter
@Getter
@SQLDelete(sql = "UPDATE `post` SET removed_at = NOW() WHERE post_id=?")
@SQLRestriction("removed_at is NULL")
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Integer id;

    private PostCategory category;

    private String title;

    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

    @Column(name = "noHtmlContents", columnDefinition = "TEXT")
    private String noHtmlContents;

    private List<String> images;

    private Integer view = 0;

//    private Boolean isPopular = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<PostViewLog> viewLogs = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Set<PostLikeUser> likesUser = new HashSet<>();

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    public List<PostViewLog> getViewLogs() {
        if (this.viewLogs == null) {
            this.viewLogs = new ArrayList<>();
        }
        return this.viewLogs;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static Post of(PostCategory category, String title, String contents, String noHtmlContents, List<String> images, User user) {
        Post entity = new Post();
        entity.setCategory(category);
        entity.setTitle(title);
        entity.setContents(contents);
        entity.setNoHtmlContents(noHtmlContents);
        entity.setImages(images);
        entity.setUser(user);
//        entity.setIsPopular(isPopular);
        return entity;
    }

}
