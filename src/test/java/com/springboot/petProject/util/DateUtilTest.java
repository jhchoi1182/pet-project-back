package com.springboot.petProject.util;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DateUtilTest {

    @DisplayName("날짜를 정해진 대로 출력한다.")
    @ParameterizedTest
    @MethodSource("dateAndResult")
    void formatResultTest(Timestamp createdAt, Boolean isYearRequired, String result) {
        String formattedResult = DateUtil.formatTimestamp(createdAt, isYearRequired);
        assertThat(formattedResult).isEqualTo(result);
    }

    private static Stream<Arguments> dateAndResult() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime lastYear = now.minusYears(1);

        return Stream.of(
                arguments(Timestamp.valueOf(now), false, now.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))),
                arguments(Timestamp.valueOf(yesterday), false, yesterday.format(java.time.format.DateTimeFormatter.ofPattern("MM.dd"))),
                arguments(Timestamp.valueOf(yesterday), true, yesterday.format(java.time.format.DateTimeFormatter.ofPattern("yy.MM.dd"))),
                arguments(Timestamp.valueOf(lastYear), false, lastYear.format(java.time.format.DateTimeFormatter.ofPattern("yy.MM.dd")))
        );
    }

}
