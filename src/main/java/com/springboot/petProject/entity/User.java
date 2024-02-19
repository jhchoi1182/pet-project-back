package com.springboot.petProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "`user`")
@Setter
@Getter
@SQLDelete(sql = "UPDATE `user` SET removed_at = NOW() WHERE user_id=?")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer id;

    @Size(min = 2, max = 16, message = "ID should have between 2 and 16 characters")
    @Column(unique = true)
    private String username;

    @Size(min = 2, max = 16, message = "ID should have between 2 and 16 characters")
    private String nickname;

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

    public static User of(String username, String nickname, String encodedPassword, UserRole role) {
        User entity = new User();
        entity.setUsername(username);
        entity.setNickname(nickname);
        entity.setPassword(encodedPassword);
        entity.setRole(role);
        return entity;
    }
}
