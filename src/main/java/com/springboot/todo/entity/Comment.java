package com.springboot.todo.entity;

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

    @Size(min = 1, message = "Todo should have atleast 1 characters")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static Comment of(User user, Todo todo, String comment) {
        Comment entity = new Comment();
        entity.setUser(user);
        entity.setTodo(todo);
        entity.setComment(comment);
        return entity;
    }

}
