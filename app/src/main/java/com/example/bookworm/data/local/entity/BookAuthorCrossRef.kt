package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "book_authors",
    primaryKeys = ["bookId", "authorId"],
    indices = [Index("bookId"), Index("authorId")],
    foreignKeys = [
        ForeignKey(entity = BookEntity::class, parentColumns = ["bookId"], childColumns = ["bookId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = AuthorEntity::class, parentColumns = ["authorId"], childColumns = ["authorId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class BookAuthorCrossRef (
    val bookId: String,
    val authorId: Long
)