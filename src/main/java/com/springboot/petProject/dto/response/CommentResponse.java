package com.springboot.petProject.dto.response;

import com.springboot.petProject.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;

@AllArgsConstructor
@Getter
public class CommentResponse {

    private Integer commentId;
    private String comment;
    private String registeredAt;

    public static CommentResponse fromDto(CommentDto comment) {
        String formattedDate =
                new SimpleDateFormat("yyyy-MM-dd")
                        .format(comment.getRegisteredAt());
        return new CommentResponse(
                comment.getCommentId(),
                comment.getComment(),
                formattedDate
        );
    }

}
