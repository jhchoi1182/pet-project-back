package com.springboot.petProject.entity;

import com.springboot.petProject.types.UserRole;
import com.springboot.petProject.types.UserType;
import jakarta.persistence.*;
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

    @Column(unique = true, name="username")
    private String username;

    @Column(unique = true)
    private String nickname;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    private UserType type;

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

    public static User of(String username, String nickname, String email, String encodedPassword, UserRole role, UserType type) {
        User entity = new User();
        entity.setUsername(username);
        entity.setNickname(nickname);
        entity.setEmail(email);
        entity.setPassword(encodedPassword);
        entity.setRole(role);
        entity.setType(type);
        return entity;
    }

}
