package com.eugeniojava.compufix.util;

import androidx.room.TypeConverter;

import java.util.Date;

public class ConverterUtil {

    private ConverterUtil() {
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toMilliseconds(Date date) {
        return date == null ? null : date.getTime();
    }
}
