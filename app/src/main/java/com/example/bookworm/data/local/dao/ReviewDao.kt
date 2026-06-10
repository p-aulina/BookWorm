package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookworm.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: ReviewEntity)

    @Query("SELECT * FROM review WHERE bookId = :bookId LIMIT 1")
    fun observeReviewForBook(bookId: String): Flow<ReviewEntity?>

    @Query("DELETE FROM review WHERE bookId = :bookId")
    suspend fun deleteForBook(bookId: String)

    @Query("UPDATE review SET rating = :rating, text = :text WHERE bookId = :bookId")
    suspend fun updateReview(bookId: String, rating: Float, text: String?)
}