package com.example.bookworm.domain.model

data class Book (
    val bookId: String,
    val title: String,
    val author: List<String>?,
    val pageCount: Int,
    val genres: List<String>,
    val datePublished: String,
    val coverURL: String?,
    val description: String,
    val language: String,

    val dateAddedToLibrary: Long,
    val dateStarted: Long?,
    val dateFinished: Long?,
    val dateLastUpdate: Long,
    val pageProgress: Int?,

    val format: BookFormat,
    val ownership: OwnershipStatus,
    val returnDate: Long? = null,
    val status: BookStatus,
)