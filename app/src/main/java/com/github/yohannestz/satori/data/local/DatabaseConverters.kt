package com.github.yohannestz.satori.data.local

import androidx.room.TypeConverter
import java.util.Date

object DatabaseConverters {

    @TypeConverter
    fun timestampToLocalDateTime(value: Long?): Date? {
        return value?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun localDateTimeToTimestamp(value: Date?): Long? {
        return value?.time
    }
}
