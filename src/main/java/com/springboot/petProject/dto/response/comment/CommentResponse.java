package com.springboot.petProject.dto.response.comment;

import com.springboot.petProject.dto.CommentDto;
import com.springboot.petProject.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;

@AllArgsConstructor
@Getter
public class CommentResponse {

    private Integer commentId;
    private String comment;
    private String nickname;
    private String formattedRegisteredAt;

    public static CommentResponse fromDto(CommentDto comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getComment(),
                comment.getNickname(),
                DateUtil.formatTimestamp(comment.getRegisteredAt())
        );
    }

}
