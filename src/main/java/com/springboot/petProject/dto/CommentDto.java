package com.springboot.petProject.dto;

import com.springboot.petProject.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class CommentDto {

    private Integer commentId;
    private String comment;
    private Integer postId;
    private String nickname;
    private Timestamp registeredAt;

    public static CommentDto fromEntity(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getComment(),
                entity.getPost().getId(),
                entity.getUser().getNickname(),
                entity.getRegisteredAt()
        );
    }

}
