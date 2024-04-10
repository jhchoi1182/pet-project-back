package com.springboot.petProject.dto;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.types.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Getter
public class DetailPostDto {

    private Integer postId;
    private PostCategory category;
    private String title;
    private String contents;
    private String noHtmlContents;
    private List<String> images;
    private String nickname;
    private Integer view;
    private Integer likes;
    private Boolean hasLiked;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static DetailPostDto fromEntity(Post entity) {
        return new DetailPostDto(
                entity.getId(),
                entity.getCategory(),
                entity.getTitle(),
                entity.getContents(),
                entity.getNoHtmlContents(),
                entity.getImages(),
                entity.getUser().getNickname(),
                entity.getView(),
                entity.getLikesUser().size(),
                false,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static DetailPostDto fromEntity(Post entity, Integer userId) {
        Boolean hasLiked = entity.hasLikedByUser(userId);

        return new DetailPostDto(
                entity.getId(),
                entity.getCategory(),
                entity.getTitle(),
                entity.getContents(),
                entity.getNoHtmlContents(),
                entity.getImages(),
                entity.getUser().getNickname(),
                entity.getView(),
                entity.getLikesUser().size(),
                hasLiked,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
