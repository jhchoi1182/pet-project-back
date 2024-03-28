package com.springboot.petProject.dto;

import com.springboot.petProject.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class DetailPostDto {

    private Integer postId;
    private String title;
    private String contents;
    private String nickname;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static DetailPostDto fromEntity(Post entity) {
        return new DetailPostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContents(),
                entity.getUser().getNickname(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
