package com.springboot.petProject.service.post;

public class PostViewCountManagerTest {

//    private final PostViewCountManager postViewCountManager = new PostViewCountManager();
//    private Post post;
//    private JSONObject viewRecords;
//    private Integer postId;
//
//    @BeforeEach
//    void setup() {
//        post = new Post();
//        viewRecords = new JSONObject();
//        postId = 1;
//    }
//
//    @Test
//    @DisplayName("쿠키가 있고, postId 조회 기록이 24시간 경과했을 때 조회수 증가")
//    void viewCountIncreasesWithCookieAfter24Hours() {
//        addNewViewLog(post, "0:0:0:0", 23);
//        addNewViewLog(post, "0:0:0:1", 0);
//        String remoteAddr = "0:0:0:0";
//        viewRecords.put(postId.toString(), LocalDateTime.now(KOREA_TIME).minusHours(25).toString());
//        viewRecords.put(String.valueOf(2), LocalDateTime.now(KOREA_TIME).toString());
//        Cookie cookie = new Cookie("viewRecord", viewRecords.toString());
//        assertThat(postViewCountManager.shouldIncreaseViewCount(post, Optional.of(cookie), remoteAddr, viewRecords, postId)).isTrue();
//    }
//
//    @Test
//    @DisplayName("쿠키가 있고, postId 조회 기록이 24시간 경과하지 않은 경우 조회수 증가하지 않음")
//    void viewCountStaysWithCookieUnder24Hours() {
//        addNewViewLog(post, "0:0:0:0", 25);
//        addNewViewLog(post, "0:0:0:1", 0);
//        String remoteAddr = "0:0:0:0";
//        viewRecords.put(postId.toString(), LocalDateTime.now(KOREA_TIME).minusHours(23).toString());
//        viewRecords.put(String.valueOf(2), LocalDateTime.now(KOREA_TIME).minusHours(25).toString());
//        Cookie cookie = new Cookie("viewRecord", viewRecords.toString());
//        assertThat(postViewCountManager.shouldIncreaseViewCount(post, Optional.of(cookie), remoteAddr, viewRecords, postId)).isFalse();
//    }
//
//    @Test
//    @DisplayName("쿠키가 있고, postId에 해당하는 뷰 레코드가 없을 때 조회수 증가")
//    void viewCountStaysWithCookieAndNoPostRecord() {
//        addNewViewLog(post, "0:0:0:0", 23);
//        addNewViewLog(post, "0:0:0:1", 0);
//        String remoteAddr = "0:0:0:0";
//        viewRecords.put(String.valueOf(2), LocalDateTime.now(KOREA_TIME).minusHours(25).toString());
//        Cookie cookie = new Cookie("viewRecord", viewRecords.toString());
//        assertThat(postViewCountManager.shouldIncreaseViewCount(post, Optional.of(cookie), remoteAddr, viewRecords, postId)).isTrue();
//    }
//
//    @Test
//    @DisplayName("쿠키가 없고, post 테이블의 viewLogs에 IP 조회 기록이 24시간 경과했을 때 조회수 증가")
//    void viewCountIncreasesWithoutCookieAfter24Hours() {
//        addNewViewLog(post, "0:0:0:0", 25);
//        addNewViewLog(post, "0:0:0:1", 0);
//        String remoteAddr = "0:0:0:0";
//        assertThat(postViewCountManager.shouldIncreaseViewCount(post, Optional.empty(), remoteAddr, viewRecords, postId)).isTrue();
//    }
//
//    @Test
//    @DisplayName("쿠키가 없고, post 테이블의 viewLogs에 IP 조회 기록이 24시간 경과하지 않은 경우 조회수 증가하지 않음")
//    void viewCountStaysWithoutCookieUnder24Hours() {
//        addNewViewLog(post, "0:0:0:0", 23);
//        addNewViewLog(post, "0:0:0:1", 25);
//        String remoteAddr = "0:0:0:0";
//        assertThat(postViewCountManager.shouldIncreaseViewCount(post, Optional.empty(), remoteAddr, viewRecords, postId)).isFalse();
//    }
//
//    @Test
//    @DisplayName("쿠키가 없고, post 테이블의 viewLogs에 IP 조회 기록이 없을 때 조회수 증가")
//    void viewCountStaysWithoutCookieNoIPRecord() {
//        addNewViewLog(post, "0:0:0:0", 23);
//        addNewViewLog(post, "0:0:0:1", 25);
//        String remoteAddr = "0:0:0:3";;
//        assertThat(postViewCountManager.shouldIncreaseViewCount(post, Optional.empty(), remoteAddr, viewRecords, postId)).isTrue();
//    }
//
//    private void addNewViewLog(Post post, String remoteAddr, Integer minusHours) {
//        PostViewIPLog newLog = new PostViewIPLog();
//        newLog.setIpAddress(remoteAddr);
//        newLog.setViewedAt(Timestamp.valueOf(LocalDateTime.now(KOREA_TIME).minusHours(minusHours)));
//        newLog.setPost(post);
//        post.getViewIPLogs().add(newLog);
//    }
}
