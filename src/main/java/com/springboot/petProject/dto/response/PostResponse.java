package com.springboot.petProject.dto.response;

import com.springboot.petProject.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponse {

    private Integer postId;
    private String title;
    private String contents;

    public static PostResponse fromDto(PostDto post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContents()
        );
    }

}