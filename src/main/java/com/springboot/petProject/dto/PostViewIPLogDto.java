package com.springboot.petProject.dto;

import com.springboot.petProject.entity.PostViewIPLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostViewIPLogDto {

    private Integer postViewLogId;
    private String ipAddress;
    private Timestamp viewedAt;

    public static PostViewIPLogDto fromEntity(PostViewIPLog entity) {
        return new PostViewIPLogDto(
                entity.getId(),
                entity.getIpAddress(),
                entity.getViewedAt()
        );
    }

}
