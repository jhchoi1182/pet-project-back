package com.springboot.petProject.dto;

import com.springboot.petProject.entity.PostViewUserLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostViewUserLogDto {

    private Integer postViewLogId;
    private String username;
    private Timestamp viewedAt;

    public static PostViewUserLogDto fromEntity(PostViewUserLog entity) {
        return new PostViewUserLogDto(
                entity.getId(),
                entity.getUser().getUsername(),
                entity.getViewedAt()
        );
    }

}
