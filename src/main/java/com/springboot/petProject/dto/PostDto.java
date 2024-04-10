package com.springboot.petProject.dto;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.types.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Getter
public class PostDto {

    private Integer postId;
    private PostCategory category;
    private String title;
    private String contents;
    private List<String> images;
    private String nickname;
    private Integer commentsCount;
    private Integer view;
    private Integer likes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static PostDto fromEntity(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getCategory(),
                entity.getTitle(),
                entity.getContents(),
                entity.getImages(),
                entity.getUser().getNickname(),
                entity.getComments().size(),
                entity.getView(),
                entity.getLikesUser().size(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
