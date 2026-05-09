package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "book_genres",
    primaryKeys = ["bookId", "genreId"],
    indices = [Index("bookId"), Index("genreId")],
    foreignKeys = [
        ForeignKey(entity = BookEntity::class, parentColumns = ["bookId"], childColumns = ["bookId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = GenreEntity::class, parentColumns = ["genreId"], childColumns = ["genreId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class BookGenreCrossRef (
    val bookId: String,
    val genreId: Long
)