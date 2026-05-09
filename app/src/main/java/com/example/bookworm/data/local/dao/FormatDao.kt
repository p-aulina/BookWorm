package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.data.local.entity.FormatEntity

@Dao
interface FormatDao {
    @Upsert
    suspend fun upsertFormat(user: FormatEntity)

    @Query("SELECT * FROM book_format WHERE bookId = :bookId")
    suspend fun selectByBookId(bookId: String): FormatEntity?
}