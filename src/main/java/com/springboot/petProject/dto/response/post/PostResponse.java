package com.springboot.petProject.dto.response.post;

import com.springboot.petProject.dto.DetailPostDto;
import com.springboot.petProject.types.PostCategory;
import com.springboot.petProject.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostResponse {

    private Integer postId;
    private PostCategory category;
    private String title;
    private String contents;
    private String noHtmlContents;
    private List<String> images;
    private String nickname;
    private Integer views;
    private Integer likes;
    private Boolean hasLiked;
    private Boolean isPopular;
    private String createdAt;

    public static PostResponse fromDto(DetailPostDto post) {
        return new PostResponse(
                post.getPostId(),
                post.getCategory(),
                post.getTitle(),
                post.getContents(),
                post.getNoHtmlContents(),
                post.getImages(),
                post.getNickname(),
                post.getViews(),
                post.getLikes(),
                post.getHasLiked(),
                post.getIsPopular(),
                DateUtil.formatTimestamp(post.getCreatedAt(), true)
        );
    }

}
