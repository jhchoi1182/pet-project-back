package com.springboot.petProject.dto.response.post;

import com.springboot.petProject.dto.DetailPostDto;
import com.springboot.petProject.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostResponse {

    private Integer postId;
    private String title;
    private String contents;
    private List<String> images;
    private String nickname;
    private String createdAt;

    public static PostResponse fromDto(DetailPostDto post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContents(),
                post.getImages(),
                post.getNickname(),
                DateUtil.formatTimestamp(post.getCreatedAt(), true)
        );
    }

}
