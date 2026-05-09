package com.example.bookworm.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageLinksDto (
    @Json(name = "thumbnail") val cover: String? = null,
    @Json(name = "smallThumbnail") val smallCover: String? = null
)