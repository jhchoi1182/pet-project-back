package com.springboot.todo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "`user`")
@Setter
@Getter
@SQLDelete(sql = "UPDATE `user` SET removed_at = NOW() WHERE user_id=?")
@SQLRestriction("removed_at is NULL")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer id;

    @Size(min = 2, message = "ID should have atleast 2 characters")
    @Column(unique = true)
    private String username;

    @Size(min = 4, message = "ID should have atleast 4 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

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

    public static User of(String username, String encodedPassword, UserRole role) {
        User entity = new User();
        entity.setUsername(username);
        entity.setPassword(encodedPassword);
        entity.setRole(role);
        return entity;
    }
}
