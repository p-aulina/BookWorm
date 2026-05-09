package com.example.bookworm.data.remote.api

import com.example.bookworm.BuildConfig
import com.example.bookworm.data.remote.dto.ApiResponseDto
import com.example.bookworm.data.remote.dto.VolumeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("startIndex") startIndex: Int = 0,
        @Query("langRestrict") language: String? = null,
        @Query("orderBy") orderBy: String = "relevance",
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): ApiResponseDto

    @GET("volumes/{volumeId}")
    suspend fun getBookById(
        @Path("volumeId") volumeId: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): VolumeDto
}