package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity (
    @PrimaryKey(autoGenerate = true) val genreId: Long = 0,
    val genreName: String
)