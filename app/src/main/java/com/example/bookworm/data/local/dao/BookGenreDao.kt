package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookworm.data.local.entity.BookGenreCrossRef

@Dao
interface BookGenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: BookGenreCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(crossRef: List<BookGenreCrossRef>)

    @Query("DELETE FROM book_genres WHERE bookId = :bookId")
    suspend fun deleteAllForBook(bookId: String)

    @Query("SELECT genreId FROM book_genres WHERE bookId = :bookId")
    suspend fun getGenreIdsForBook(bookId: String): List<Long>
}