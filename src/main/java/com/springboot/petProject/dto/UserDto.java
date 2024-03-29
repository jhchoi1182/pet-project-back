package com.springboot.petProject.dto;

import com.springboot.petProject.entity.User;
import com.springboot.petProject.types.UserRole;
import com.springboot.petProject.types.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
public class UserDto implements UserDetails {

    private Integer userId;
    private String username;
    private String nickname;
    private String email;
    private String password;
    private UserRole role;
    private UserType type;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static UserDto fromEntity(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getUsername(),
                entity.getNickname(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getType(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }
    @Override
    public boolean isAccountNonExpired() {
        return removedAt == null;
    }
    @Override
    public boolean isAccountNonLocked() {
        return removedAt == null;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return removedAt == null;
    }
    @Override
    public boolean isEnabled() {
        return removedAt == null;
    }
}
