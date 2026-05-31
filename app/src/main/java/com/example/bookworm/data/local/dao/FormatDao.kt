package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.data.local.entity.FormatEntity
import com.example.bookworm.domain.model.BookFormat
import kotlinx.coroutines.flow.Flow

@Dao
interface FormatDao {
    @Upsert
    suspend fun upsertFormat(user: FormatEntity)

    @Query("SELECT * FROM book_format WHERE bookId = :bookId")
    suspend fun selectByBookId(bookId: String): FormatEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(format: FormatEntity)

    @Delete
    suspend fun delete(format: FormatEntity)

    @Query("SELECT format FROM book_format WHERE bookId = :bookId")
    fun observeFormatsForBook(bookId: String): Flow<List<BookFormat>>

    @Query("SELECT format FROM book_format WHERE bookId = :bookId")
    suspend fun getFormatsForBook(bookId: String): List<BookFormat>

    @Query("DELETE FROM book_format WHERE bookId = :bookId")
    suspend fun deleteAllForBook(bookId: String)
}