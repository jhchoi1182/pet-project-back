package com.springboot.petProject.service.post;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PostSearchSpecification {
    public Specification<Post> search(String type, String value) {
        switch (type) {
            case "all":
                return Specification.where(titleContains(value))
                        .or(contentsContains(value));
            case "title":
                return titleContains(value);
            case "contents":
                return contentsContains(value);
            case "nickname":
                return userNicknameContains(value);
            default:
                return null; // 혹은 모든 게시글을 반환하는 Specification
        }
    }

    private Specification<Post> titleContains(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    private Specification<Post> contentsContains(String contents) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("noHtmlContents"), "%" + contents + "%");
    }

    private Specification<Post> userNicknameContains(String nickname) {
        return (root, query, criteriaBuilder) -> {
            Join<Post, User> userJoin = root.join("user");
            return criteriaBuilder.like(userJoin.get("nickname"), "%" + nickname + "%");
        };
    }
}
