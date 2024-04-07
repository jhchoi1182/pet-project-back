package com.springboot.petProject.dto;

import com.springboot.petProject.entity.PostViewLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostViewLogDto {

    private Integer postViewLogId;
    private String ipAddress;
    private Timestamp viewedAt;

    public static PostViewLogDto fromEntity(PostViewLog entity) {
        return new PostViewLogDto(
                entity.getId(),
                entity.getIpAddress(),
                entity.getViewedAt()
        );
    }

}
