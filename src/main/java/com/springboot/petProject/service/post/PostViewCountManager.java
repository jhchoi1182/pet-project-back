package com.springboot.petProject.service.post;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.PostViewIPLog;
import com.springboot.petProject.entity.PostViewUserLog;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.repository.PostViewIPLogRepository;
import com.springboot.petProject.repository.PostViewUserLogRepository;
import com.springboot.petProject.service.ExceptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.springboot.petProject.util.DateUtil.KOREA_TIME;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostViewCountManager {

    private final PostViewIPLogRepository postViewIPLogRepository;
    private final PostViewUserLogRepository postViewUserLogRepository;
    private final ExceptionService exceptionService;

    public boolean shouldIncreaseViewCount(Post post, String remoteAddr, UserDto userDto) {
        if (userDto == null) {
            return shouldIncreaseViewBasedOnIP(post, remoteAddr);
        } else {
            User user = exceptionService.getUserOrThrowException(userDto.getUsername());
            return shouldIncreaseViewBasedOnUser(post, user);
        }
    }

    private boolean shouldIncreaseViewBasedOnIP(Post post, String remoteAddr) {
        Optional<PostViewIPLog> existingIPLog = postViewIPLogRepository.findByPostIdAndIpAddress(post.getId(), remoteAddr);
        if (!existingIPLog.isPresent()) {
            updateNewViewIPLog(post, remoteAddr);
            return true;
        } else {
            return updateViewIPLogIf24HoursElapsed(existingIPLog.get());
        }
    }

    private boolean shouldIncreaseViewBasedOnUser(Post post, User user) {
        Optional<PostViewUserLog> existingUserLog = postViewUserLogRepository.findByPostIdAndUserId(post.getId(), user.getId());
        if (!existingUserLog.isPresent()) {
            updateNewViewUserLog(post, user);
            return true;
        } else {
            return updateViewUserLogIf24HoursElapsed(existingUserLog.get());
        }
    }

    private void updateNewViewIPLog(Post post, String remoteAddr) {
        PostViewIPLog newLog = PostViewIPLog.add(remoteAddr, post);
        post.getViewIPLogs().add(newLog);
    }
    private void updateNewViewUserLog(Post post, User user) {
        PostViewUserLog newLog = PostViewUserLog.add(post, user);
        post.getViewUserLogs().add(newLog);
    }

    private boolean updateViewIPLogIf24HoursElapsed(PostViewIPLog existingIPLog) {
        LocalDateTime lastViewDateTime = existingIPLog.getViewedAt().toLocalDateTime();
        if (ChronoUnit.HOURS.between(lastViewDateTime, LocalDateTime.now(KOREA_TIME)) > 24) {
            existingIPLog.setViewedAt(Timestamp.valueOf(LocalDateTime.now()));
            return true;
        }
        return false;
    }

    private boolean updateViewUserLogIf24HoursElapsed(PostViewUserLog existingUserLog) {
        LocalDateTime lastViewDateTime = existingUserLog.getViewedAt().toLocalDateTime();
        if (ChronoUnit.HOURS.between(lastViewDateTime, LocalDateTime.now(KOREA_TIME)) > 24) {
            existingUserLog.setViewedAt(Timestamp.valueOf(LocalDateTime.now(KOREA_TIME)));
            return true;
        }
        return false;
    }

}
