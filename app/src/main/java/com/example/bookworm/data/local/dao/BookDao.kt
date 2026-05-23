package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.bookworm.data.local.entity.BookEntity
import com.example.bookworm.data.local.entity.BookWithDetails
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    //INSERT OR UPDATE
    @Upsert
    suspend fun upsertBook(user: BookEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(book: BookEntity): Long

    //DELETE
    @Query("DELETE FROM books WHERE bookId = :bookId")
    suspend fun delete(bookId: String)

    //SELECT ALL BOOKS
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>

    //SELECT A BOOK
    @Query("SELECT * FROM books WHERE bookId = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    //UPDATE READING STATUS
    @Query("UPDATE books SET status = :status WHERE bookId = :bookId")
    suspend fun updateStatus(bookId: String, status: BookStatus)

    //UPDATE FORMAT
//    @Query("UPDATE books SET format = :format WHERE bookId = :bookId")
//    suspend fun updateFormat(bookId: String, format: BookFormat)

    //UPDATE OWNERSHIP
    @Query("UPDATE books SET ownership = :ownership WHERE bookId = :bookId")
    suspend fun updateOwnership(bookId: String, ownership: OwnershipStatus)

    //UPDATE - started reading
    @Query("UPDATE books SET dateStarted = :timestamp WHERE bookId = :bookId")
    suspend fun updateDateStarted(bookId: String, timestamp: Long)

    //SELECT BY STATUS
    @Query("SELECT * FROM books WHERE status = :status")
    fun getBookByStatus(status: BookStatus): Flow<List<BookEntity>>

    @Query("SELECT COUNT(*) FROM books")
    suspend fun getBookCount(): Int

    //SELECT BY OWNERSHIP
    @Query("SELECT * FROM books WHERE ownership = :ownership")
    suspend fun getBookByOwnership(ownership: OwnershipStatus): List<BookEntity>

    @Transaction
    @Query("SELECT * FROM books WHERE ownership = :ownership")
    fun observeBooksByOwnership(ownership: OwnershipStatus): Flow<List<BookWithDetails>>


    @Transaction
    @Query("SELECT * FROM books WHERE bookId = :bookId")
    fun observeBookWithDetails(bookId: String): Flow<BookWithDetails?>

    @Transaction
    @Query("SELECT * FROM books")
    fun observeAllBooksWithDetails(): Flow<List<BookWithDetails>>

    @Transaction
    @Query("SELECT * FROM books WHERE status = :status")
    fun observeBooksWithDetailsByStatus(status: BookStatus): Flow<List<BookWithDetails>>
}