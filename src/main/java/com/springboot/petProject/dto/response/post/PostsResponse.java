package com.springboot.petProject.dto.response.post;

import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostsResponse {

    private Integer postId;
    private String title;
    private String nickname;
    private int commentsCount;
    private String createdAt;
    private Timestamp updatedAt;

    public static PostsResponse fromDto(PostDto post) {
        return new PostsResponse(
                post.getPostId(),
                post.getTitle(),
                post.getNickname(),
                post.getCommentsCount(),
                DateUtil.formatTimestamp(post.getCreatedAt()),
                post.getUpdatedAt()
        );
    }

}
