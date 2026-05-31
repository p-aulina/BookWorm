package com.example.bookworm.data.repository

import com.example.bookworm.data.local.dao.ReviewDao
import com.example.bookworm.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDao: ReviewDao
){
    fun observeReviewForBook(bookId: String): Flow<ReviewEntity?> =
        reviewDao.observeReviewForBook(bookId)

    suspend fun addReview(bookId: String, rating: Float, text: String?){
        reviewDao.insert(
            ReviewEntity(
                bookId = bookId,
                rating = rating,
                text = text?.takeIf { it.isNotBlank() },
                date = System.currentTimeMillis(),
            )
        )
    }

    suspend fun deleteReview(bookId: String) =
        reviewDao.deleteForBook(bookId)
}