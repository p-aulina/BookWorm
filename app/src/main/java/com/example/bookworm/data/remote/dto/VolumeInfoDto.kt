package com.example.bookworm.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VolumeInfoDto(
    @Json(name = "title") val title: String = "Unknown",
    @Json(name = "authors") val authors: List<String>? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "categories") val genres: List<String>? = null,
    @Json(name = "pageCount") val pageCount: Int? = null,
    @Json(name = "publishedDate") val datePublished: String? = null,
    @Json(name = "language") val language: String? = null,
    @Json(name = "imageLinks") val cover: ImageLinksDto? = null
)
