package com.springboot.petProject.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String formatTimestamp(Timestamp timestamp, Boolean isYearRequired) {

        Instant instant = timestamp.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Seoul"));
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        LocalDateTime todayMidnight = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
        LocalDateTime yesterdayMidnight = todayMidnight.minusDays(1);
        LocalDateTime firstDayOfYear = LocalDateTime.of(now.getYear(), 1, 1, 0, 0);
        LocalDateTime targetTime = zonedDateTime.toLocalDateTime();

        if (targetTime.isBefore(yesterdayMidnight)) {
            if (isYearRequired || targetTime.isBefore(firstDayOfYear)) return targetTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            else return targetTime.format(DateTimeFormatter.ofPattern("MM.dd"));
        } else {
            return targetTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
    }

    public static String formatTimestamp(Timestamp timestamp) {
        return formatTimestamp(timestamp, false);
    }
}
