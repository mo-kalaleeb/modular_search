package com.ticketswap.cache.converter

import androidx.room.TypeConverter
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.Date

class CacheConverters {
    @TypeConverter
    fun stringToResponse(value: String?): Response {
        val responseBody = (value ?: "").toResponseBody()
        return Response.Builder()
            .body(responseBody)
            .build()
    }

    @TypeConverter
    fun responseToString(response: Response): String {
        return response.body.toString()
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
