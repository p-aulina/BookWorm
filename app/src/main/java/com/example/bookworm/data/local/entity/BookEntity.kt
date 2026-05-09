package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus

@Entity(tableName = "books")
data class BookEntity (
    //API fields
    @PrimaryKey val bookId: String,
    val title: String,
    val pageCount: Int,
    val datePublished: String,
    val coverURL: String?,
    val description: String,
    val language: String,

    //user fields
    val dateAddedToLibrary: Long = System.currentTimeMillis(),
    val dateStarted: Long? = null,
    val dateFinished: Long? = null,
    val dateLastUpdate: Long = System.currentTimeMillis(),
    val pageProgress: Int = 0,

    //val ownedFormats: Set<BookFormat>,
    val ownership: OwnershipStatus,
    val returnDate: Long? = null,
    val status: BookStatus = BookStatus.TBR
)