package com.example.bookworm.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bookworm.domain.model.BookFormat

data class BookWithDetails (
    @Embedded val book: BookEntity,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "authorId",
        associateBy = Junction(BookAuthorCrossRef::class)
    )
    val authors: List<AuthorEntity>,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "genreId",
        associateBy = Junction(BookGenreCrossRef::class)
    )
    val genres: List<GenreEntity>,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val formats: Set<FormatEntity>
)