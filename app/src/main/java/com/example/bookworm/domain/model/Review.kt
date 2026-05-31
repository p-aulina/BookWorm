package com.example.bookworm.domain.model

data class Review(
    val bookId: String,
    val rating: Float,
    val review: String? = null,
    val date: Long,
    val isEdited: Boolean = false
)
