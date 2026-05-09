package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookworm.data.local.entity.AuthorEntity


@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(author: AuthorEntity): Long

    @Query("SELECT * FROM authors WHERE authorName = :name")
    suspend fun getByName(name: String): AuthorEntity?

    @Query("SELECT * FROM authors WHERE authorId = :authorId")
    suspend fun getById(authorId: Long): AuthorEntity?
}