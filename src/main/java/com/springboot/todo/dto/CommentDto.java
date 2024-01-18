package com.springboot.todo.dto;

import com.springboot.todo.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class CommentDto {

    private Integer commentId;
    private String comment;
    private Integer todoId;
    private String username;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static CommentDto fromEntity(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getComment(),
                entity.getTodo().getId(),
                entity.getUser().getUsername(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt()
        );
    }

}
