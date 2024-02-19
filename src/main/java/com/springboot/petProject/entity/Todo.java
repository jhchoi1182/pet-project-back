package com.springboot.petProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "`todo`")
@Setter
@Getter
@SQLDelete(sql = "UPDATE `todo` SET removed_at = NOW() WHERE todo_id=?")
@SQLRestriction("removed_at is NULL")
public class Todo {

    @Id
    @GeneratedValue
    @Column(name = "todo_id")
    private Integer id;

    @Size(min = 1, message = "Todo should have atleast 1 characters")
    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private List<Comment> comments;

    private Boolean isDone = false;

    @Column(name = "due_date")
    private String dueDate;

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

    public static Todo of(String contents, String dueDate, User user) {
        Todo entity = new Todo();
        entity.setContents(contents);
        entity.setDueDate(dueDate);
        entity.setUser(user);
        return entity;
    }

}