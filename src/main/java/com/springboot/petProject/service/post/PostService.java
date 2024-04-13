package com.springboot.petProject.service.post;

import com.springboot.petProject.dto.DetailPostDto;
import com.springboot.petProject.dto.PostDto;
import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.PostLikeUser;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.repository.CommentRepository;
import com.springboot.petProject.repository.PostLikeUserRepository;
import com.springboot.petProject.repository.PostRepository;
import com.springboot.petProject.repository.PostViewIPLogRepository;
import com.springboot.petProject.service.ExceptionService;
import com.springboot.petProject.service.S3UploadService;
import com.springboot.petProject.service.user.CookieService;
import com.springboot.petProject.types.PostCategory;
import com.springboot.petProject.types.request.CategoryRequest;
import com.springboot.petProject.types.request.SearchType;
import com.springboot.petProject.util.HtmlTextUtil;
import com.springboot.petProject.util.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostViewIPLogRepository postViewIPLogRepository;
    private final PostLikeUserRepository postLikeUserRepository;
    private final ExceptionService exceptionService;
    private final S3UploadService s3UploadService;
    private final CookieService cookieService;
    private final PostSearchSpecification postSearchSpecification;
    private final PostViewCountManager postViewCountManager;
    private final Validate validate;

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PostDto> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostDto::fromEntity);
    }

    public Page<PostDto> searchPosts(CategoryRequest category, SearchType searchType, String value, Pageable pageable) {
        Specification<Post> spec = postSearchSpecification.search(category, searchType, value);
        return postRepository.findAll(spec, pageable).map(PostDto::fromEntity);
    }

    public DetailPostDto getPostForISR(Integer postId) {
        Post post = exceptionService.getPostOrThrowException(postId);
        return DetailPostDto.fromEntity(post);
    }

    public DetailPostDto getPost(Integer postId, String remoteAddr, Authentication authentication) {
        Post post = exceptionService.getPostOrThrowException(postId);

        UserDto userDto = null;
        if (authentication != null) {
            userDto = exceptionService.getAuthenticationPrincipal(authentication);
        }

        boolean shouldIncreaseView = postViewCountManager.shouldIncreaseViewCount(post, remoteAddr, userDto);

        if (shouldIncreaseView) {
            post.setViews(post.getViews() + 1);
        }

        return DetailPostDto.fromEntity(postRepository.save(post), userDto);
    }

    @Transactional
    public void create(PostCategory category ,String title, String contents, List<String> base64Images, String username) {
        validateTitleAndContentsNotNull(title, contents);
        validate.validateBadWord(title);
        validate.validateBadWord(contents);

        User user = exceptionService.getUserOrThrowException(username);
        String filteredContents = HtmlTextUtil.extractTextFromHtml(contents);
        List<String> images = uploadImagesToS3(base64Images);

        postRepository.save(Post.of(category, title, contents, filteredContents, images, user, false));
    }

    private List<String> uploadImagesToS3(List<String> base64Images) {
        List<String> images = new ArrayList<>();
        if (!base64Images.isEmpty()) {
            images = s3UploadService.uploadBase64Images(base64Images);
        }
        return images;
    }

    @Transactional
    public DetailPostDto update(Integer postId, PostCategory category, String title, String contents, List<String> base64Images, Integer userId) {
        validateTitleAndContentsNotNull(title, contents);
        validate.validateBadWord(title);
        validate.validateBadWord(contents);
        Post post = exceptionService.getPostIfAuthorized(postId, userId);
        String filteredContents = HtmlTextUtil.extractTextFromHtml(contents);
        List<String> images = uploadImagesToS3(base64Images);

        post.setCategory(category);
        post.setTitle(title);
        post.setContents(contents);
        post.setNoHtmlContents(filteredContents);
        post.setImages(images);

        return DetailPostDto.fromEntity(postRepository.save(post));
    }

    private void validateTitleAndContentsNotNull(String title, String contents) {
        if (title == null || contents == null) {
            throw new CustomExceptionHandler(ErrorCode.HTTP_MESSAGE_NOT_READABLE);
        }
    }

    @Transactional
    public void updatePostLike(Integer postId, Integer userId, String username) {
        Optional<PostLikeUser> existingLike = postLikeUserRepository.findByPostIdAndUserId(postId, userId);
        Post post = exceptionService.getPostOrThrowException(postId);

        if (existingLike.isPresent()) {
            postLikeUserRepository.delete(existingLike.get());
        } else {
            User user = exceptionService.getUserOrThrowException(username);
            postLikeUserRepository.save(PostLikeUser.add(post, user));
        }
        Integer likes = postLikeUserRepository.countLikesByPostId(postId);
        setPopular(likes, post);
    }

    private void setPopular(Integer likes, Post post) {
        boolean isPopular = likes > 15;
        if (post.getIsPopular() != isPopular) {
            post.setIsPopular(isPopular);
            postRepository.save(post);
        }
    }

    @Transactional
    public void deletePost(Integer postId, Integer userId) {
        Post post = exceptionService.getPostIfAuthorized(postId, userId);
        commentRepository.deleteAllByPost(post);
        postViewIPLogRepository.deleteAllByPost(post);
        postLikeUserRepository.deleteAllByPost(post);
        postRepository.deleteByPost(post);
    }


}
