package com.springboot.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private  long userId;
    @Size(min = 2, message = "ID should have atleast 2 characters")
    @Column(unique = true)
    private String username;
    @Size(min = 4, message = "ID should have atleast 4 characters")
    private String password;

    public User(long id, String username, String password) {
        this.userId = id;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return userId;
    }

    public void setId(long id) {
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
