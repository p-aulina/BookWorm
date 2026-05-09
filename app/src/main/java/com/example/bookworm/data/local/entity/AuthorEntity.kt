package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class AuthorEntity (
    @PrimaryKey(autoGenerate = true) val authorId: Long = 0,
    val authorName: String
)