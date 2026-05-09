package com.example.bookworm.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseDto (
    @Json(name = "totalItems") val totalItems: Int = 0,
    @Json(name = "items") val items: List<VolumeDto>? = null
)