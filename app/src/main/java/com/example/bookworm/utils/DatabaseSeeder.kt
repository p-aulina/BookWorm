package com.example.bookworm.utils

import com.example.bookworm.data.repository.BookRepository
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val bookRepository: BookRepository
){
    suspend fun seed(){
        val testBooks = listOf(
            Book(
                bookId = "test_001",
                title = "Dune",
                author = listOf("Frank Herbert"),
                genres = listOf("Science Fiction", "Adventure"),
                description = "A science fiction epic set on the desert planet Arrakis.",
                coverURL = null,
                pageCount = 412,
                datePublished = "1965",
                language = "en",
                status = BookStatus.FINISHED,
                ownedFormats = setOf(BookFormat.PHYSICAL),
                ownership = OwnershipStatus.OWNED,
                dateAddedToLibrary = System.currentTimeMillis(),
                dateStarted = null,
                dateFinished = null,
                dateLastUpdate = System.currentTimeMillis(),
                pageProgress = null
            ),
            Book(
                bookId = "test_002",
                title = "The Name of the Wind",
                author = listOf("Patrick Rothfuss"),
                genres = listOf("Fantasy"),
                description = "The tale of Kvothe, a legendary wizard.",
                coverURL = null,
                pageCount = 662,
                datePublished = "2007",
                language = "en",
                status = BookStatus.READING,
                ownedFormats = setOf(BookFormat.PHYSICAL, BookFormat.AUDIOBOOK),
                ownership = OwnershipStatus.OWNED,
                dateAddedToLibrary = System.currentTimeMillis(),
                dateStarted = System.currentTimeMillis(),
                dateFinished = null,
                dateLastUpdate = System.currentTimeMillis(),
                pageProgress = null
            )
        )
        testBooks.forEach { bookRepository.addBook(it) }
    }
}