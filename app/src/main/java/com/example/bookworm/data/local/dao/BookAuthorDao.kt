package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookworm.data.local.entity.BookAuthorCrossRef

@Dao
interface BookAuthorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: BookAuthorCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(crossRef: List<BookAuthorCrossRef>)

    @Query("DELETE FROM book_authors WHERE bookId = :bookId")
    suspend fun deleteAllForBook(bookId: String)

    @Query("SELECT authorId FROM book_authors WHERE bookId = :bookId")
    suspend fun getAuthorIdsForBook(bookId: String): List<Long>
}