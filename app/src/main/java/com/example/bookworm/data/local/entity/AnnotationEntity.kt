package com.example.bookworm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "annotation")
data class AnnotationEntity(
    @PrimaryKey(autoGenerate = true) val annotationId: Long = 0,
    val color: String = "#FFB300",
    val label: String
)