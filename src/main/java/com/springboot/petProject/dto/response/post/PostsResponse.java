package com.springboot.petProject.dto.response.post;

import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.types.PostCategory;
import com.springboot.petProject.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostsResponse {

    private Integer postId;
    private PostCategory category;
    private String title;
    private String nickname;
    private Integer commentsCount;
    private Integer views;
    private Integer likes;
    private Boolean isPopular;
    private String createdAt;
    private Timestamp updatedAt;

    public static PostsResponse fromDto(PostDto post) {
        return new PostsResponse(
                post.getPostId(),
                post.getCategory(),
                post.getTitle(),
                post.getNickname(),
                post.getCommentsCount(),
                post.getViews(),
                post.getLikes(),
                post.getIsPopular(),
                DateUtil.formatTimestamp(post.getCreatedAt()),
                post.getUpdatedAt()
        );
    }

}
