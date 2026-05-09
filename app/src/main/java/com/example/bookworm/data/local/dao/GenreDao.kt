package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookworm.data.local.entity.GenreEntity

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(genre: GenreEntity): Long

    @Query("SELECT * FROM genres WHERE genreName = :genreName")
    suspend fun getByName(genreName: String): GenreEntity?

    @Query("SELECT * FROM genres WHERE genreId = :genreId")
    suspend fun getById(genreId: Long): GenreEntity?
}