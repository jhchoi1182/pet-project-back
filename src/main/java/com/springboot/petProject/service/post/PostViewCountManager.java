package com.springboot.petProject.service.post;

import com.springboot.petProject.entity.Post;
import com.springboot.petProject.entity.PostViewLog;
import jakarta.servlet.http.Cookie;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PostViewCountManager {
    public boolean shouldIncreaseViewCount(Post post, Optional<Cookie> viewRecordCookie, String remoteAddr, JSONObject viewRecords, Integer postId) {
        boolean shouldIncreaseView = false;
        if (viewRecordCookie.isPresent()) {
            if (viewRecords.has(postId.toString())) {
                shouldIncreaseView = has24HoursElapsedSinceLastView(viewRecords, postId);
            } else {
                shouldIncreaseView = true;
            }
        } else {
            shouldIncreaseView = shouldIncreaseViewBasedOnIP(post, remoteAddr);
        }
        return shouldIncreaseView;
    }

    private boolean has24HoursElapsedSinceLastView(JSONObject viewRecords, Integer postId) {
        LocalDateTime lastViewDateTime = LocalDateTime.parse(viewRecords.getString(postId.toString()));
        return ChronoUnit.HOURS.between(lastViewDateTime, LocalDateTime.now()) >= 24;
    }

    private boolean shouldIncreaseViewBasedOnIP(Post post, String remoteAddr) {
        Optional<PostViewLog> currentRemoteAddrViewLog = post.getViewLogs().stream()
                .filter(log -> log.getIpAddress().equals(remoteAddr))
                .findFirst();

        if (!currentRemoteAddrViewLog.isPresent()) {
            addNewViewLog(post, remoteAddr);
            return true;
        } else {
            return updateViewLogIf24HoursElapsed(currentRemoteAddrViewLog.get());
        }
    }

    private void addNewViewLog(Post post, String remoteAddr) {
        PostViewLog newLog = new PostViewLog();
        newLog.setIpAddress(remoteAddr);
        newLog.setViewedAt(Timestamp.valueOf(LocalDateTime.now()));
        newLog.setPost(post);
        post.getViewLogs().add(newLog);
    }

    private boolean updateViewLogIf24HoursElapsed(PostViewLog currentRemoteAddrViewLog) {
        LocalDateTime lastViewDateTime = currentRemoteAddrViewLog.getViewedAt().toLocalDateTime();
        if (ChronoUnit.HOURS.between(lastViewDateTime, LocalDateTime.now()) > 24) {
            currentRemoteAddrViewLog.setViewedAt(Timestamp.valueOf(LocalDateTime.now()));
            return true;
        }
        return false;
    }

    public void increaseViewCount(Post post, Integer postId, JSONObject viewRecords) {
        post.setView(post.getView() + 1);
        viewRecords.put(postId.toString(), LocalDateTime.now().toString());
    }
}
