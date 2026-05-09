package com.example.bookworm.data.local.converters

import androidx.room.TypeConverter
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus

class Converters {
    @TypeConverter
    fun fromBookFormat(value: BookFormat): String = value.name

    @TypeConverter
    fun toBookFormat(value: String): BookFormat = enumValueOf(value)

    @TypeConverter
    fun fromOwnershipStatus(value: OwnershipStatus): String = value.name

    @TypeConverter
    fun toOwnershipStatus(value: String): OwnershipStatus = enumValueOf(value)

    @TypeConverter
    fun fromBookStatus(value: BookStatus): String = value.name

    @TypeConverter
    fun toBookStatus(value: String): BookStatus = enumValueOf(value)
}