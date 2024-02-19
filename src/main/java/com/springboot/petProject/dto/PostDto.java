package com.springboot.petProject.dto;

import com.springboot.petProject.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostDto {

    private Integer postId;
    private String contents;
    private Boolean isDone;
    private String dueDate;
    private UserDto user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static PostDto fromEntity(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getContents(),
                entity.getIsDone(),
                entity.getDueDate(),
                UserDto.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt()
        );
    }

}
