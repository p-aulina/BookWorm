package com.example.bookworm.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithAnnotation(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "annotationId",
        entityColumn = "annotationId"
    )
    val annotation: AnnotationEntity?
)
