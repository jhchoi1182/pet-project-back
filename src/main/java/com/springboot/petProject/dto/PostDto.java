package com.springboot.petProject.dto;

import com.springboot.petProject.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostDto {

    private Integer postId;
    private String title;
    private String contents;
    private String nickname;
    private int commentsCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static PostDto fromEntity(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContents(),
                entity.getUser().getNickname(),
                entity.getComments().size(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
