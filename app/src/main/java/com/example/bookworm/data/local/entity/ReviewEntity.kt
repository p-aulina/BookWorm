package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "review",
    foreignKeys = [
        ForeignKey(entity = BookEntity::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReviewEntity(
    @PrimaryKey val bookId: String,
    val rating: Float,
    val text: String? = null,
    val date: Long,
    val isEdited: Boolean = false
)
