package com.hhx.house.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author hhx
 * @since 2018/2/24 10:10
 */
public class DateUtils {

    public static LocalDateTime UDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
}
