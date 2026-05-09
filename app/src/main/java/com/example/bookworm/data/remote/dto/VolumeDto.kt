package com.example.bookworm.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VolumeDto(
    @Json(name = "id") val id: String,
    @Json(name = "volumeInfo") val volumeInfo: VolumeInfoDto
)
