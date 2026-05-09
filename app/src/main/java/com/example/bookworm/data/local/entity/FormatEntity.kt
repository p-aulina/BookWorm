package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.bookworm.domain.model.BookFormat

@Entity(
    tableName = "book_format",
    foreignKeys = [
        ForeignKey(entity = BookEntity::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["bookId", "format"]
)
data class FormatEntity(
    val bookId: String,
    val format: BookFormat
)
