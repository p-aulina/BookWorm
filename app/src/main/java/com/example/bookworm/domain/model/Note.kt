package com.example.bookworm.domain.model

data class Note(
    val noteId: Long,
    val bookId: String,
    val pageNr: Int? = null,
    val annotation: String? = null,
    val color: String? = null,
    val timestamp: Long,
    val text: String,
    val annotationLabel: String? = null,
    val annotationColor: String? = null
)
