package com.springboot.petProject.dto.response.post;

import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponse {

    private Integer postId;
    private String title;
    private String contents;
    private String nickname;
    private String registeredAt;

    public static PostResponse fromDto(PostDto post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContents(),
                post.getNickname(),
                DateUtil.formatTimestamp(post.getRegisteredAt())
        );
    }

}
