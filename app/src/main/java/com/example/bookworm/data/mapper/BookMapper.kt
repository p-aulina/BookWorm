package com.example.bookworm.data.mapper

import com.example.bookworm.data.local.entity.BookEntity
import com.example.bookworm.data.local.entity.BookWithDetails
import com.example.bookworm.domain.model.Book

object BookMapper {
    fun toDomain(bookWithDetails: BookWithDetails): Book {
        return Book(
            bookId = bookWithDetails.book.bookId,
            title = bookWithDetails.book.title,
            description = bookWithDetails.book.description,
            coverURL = bookWithDetails.book.coverURL,
            pageCount = bookWithDetails.book.pageCount,
            datePublished = bookWithDetails.book.datePublished,
            language = bookWithDetails.book.language,
            author = bookWithDetails.authors.map { it.authorName },
            genres = bookWithDetails.genres.map { it.genreName },
            status = bookWithDetails.book.status,
            format = bookWithDetails.book.format,
            ownership = bookWithDetails.book.ownership,
            dateAddedToLibrary = bookWithDetails.book.dateAddedToLibrary,
            dateStarted = bookWithDetails.book.dateStarted,
            dateFinished = bookWithDetails.book.dateFinished,
            dateLastUpdate = bookWithDetails.book.dateLastUpdate,
            pageProgress = bookWithDetails.book.pageCount
        )
    }

    fun toEntity(book: Book): BookEntity{
        return BookEntity(
            bookId = book.bookId,
            title = book.title,
            description = book.description,
            coverURL = book.coverURL,
            pageCount = book.pageCount,
            datePublished = book.datePublished,
            language = book.language,
            status = book.status,
            format = book.format,
            ownership = book.ownership,
            dateAddedToLibrary = book.dateAddedToLibrary,
            dateStarted = book.dateStarted,
            dateFinished = book.dateFinished,
            dateLastUpdate = book.dateLastUpdate,
            pageProgress = book.pageCount
        )
    }

    fun toDomainList(list: List<BookWithDetails>): List<Book> = list.map { toDomain(it) }
}