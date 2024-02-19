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
    private UserDto user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static PostDto fromEntity(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContents(),
                UserDto.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt()
        );
    }

}
