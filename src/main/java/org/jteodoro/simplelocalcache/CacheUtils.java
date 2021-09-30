package org.jteodoro.simplelocalcache;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CacheUtils {

    private CacheUtils() {}

    public static long toEpoch() {
        return toEpoch(LocalDateTime.now());
    }

    public static long toEpoch(LocalDateTime time) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zdtNow = ZonedDateTime.of(time, zone);
        return zdtNow.toEpochSecond();
    }

}
