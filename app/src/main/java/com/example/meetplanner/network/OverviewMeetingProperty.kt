package com.example.meetplanner.network

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class OverviewMeetingProperty(
    val id: Int,
    val name: String,
    val description: String
//    @Json(name="start_datetime")val startDatetime: LocalDateTime
)
