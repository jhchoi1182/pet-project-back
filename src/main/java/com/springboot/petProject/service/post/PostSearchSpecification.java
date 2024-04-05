package com.springboot.petProject.service.post;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.types.PostCategory;
import com.springboot.petProject.types.request.CategoryRequest;
import com.springboot.petProject.types.request.SearchType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PostSearchSpecification {
    public Specification<Post> search(CategoryRequest category, SearchType searchType, String value) {
        Specification<Post> categorySpec = categoryFilter(category);
        Specification<Post> searchTypeSpec = searchTypeFilter(searchType, value);
        return Specification.where(categorySpec).and(searchTypeSpec);
    }

    private Specification<Post> categoryFilter(CategoryRequest category) {
        return (root, query, criteriaBuilder) -> {
            switch (category) {
                case all:
                    return criteriaBuilder.conjunction();
                case chat:
                    return criteriaBuilder.equal(root.get("category"), PostCategory.CHAT);
                case recruit:
                    return criteriaBuilder.equal(root.get("category"), PostCategory.RECRUIT);
                case information:
                    return criteriaBuilder.equal(root.get("category"), PostCategory.INFORMATION);
                case question:
                    return criteriaBuilder.equal(root.get("category"), PostCategory.QUESTION);
                default:
                    throw new CustomExceptionHandler(ErrorCode.INVALID_REQUEST_VALUE, "Invalid category queryString");
            }
        };
    }

    private Specification<Post> searchTypeFilter(SearchType searchType, String value) {
        switch (searchType) {
            case all:
                return Specification.where(titleContains(value))
                        .or(contentsContains(value));
            case title:
                return titleContains(value);
            case contents:
                return contentsContains(value);
            case nickname:
                return userNicknameContains(value);
            default:
                throw new CustomExceptionHandler(ErrorCode.INVALID_REQUEST_VALUE, "Invalid searchType queryString");
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
