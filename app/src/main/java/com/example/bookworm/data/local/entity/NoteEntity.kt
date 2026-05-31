package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "note",
    foreignKeys = [
        ForeignKey(entity = BookEntity::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnnotationEntity::class,
            parentColumns = ["annotationId"],
            childColumns = ["annotationId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("annotationId")]
)
data class NoteEntity (
    @PrimaryKey(autoGenerate = true) val noteId: Long = 0,
    val bookId: String,
    val text: String,
    val pageNr: Int? = null,
    val annotationId: Long? = null,
    val timestamp: Long = System.currentTimeMillis()
)