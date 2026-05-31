package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookworm.data.local.entity.AnnotationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnotationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(annotation: AnnotationEntity): Long

    @Update
    suspend fun update(annotation: AnnotationEntity)

    @Delete
    suspend fun delete(annotation: AnnotationEntity)

    @Query("SELECT * FROM annotation ORDER BY label ASC")
    fun observeAll(): Flow<List<AnnotationEntity>>

    @Query("SELECT * FROM annotation WHERE annotationId = :id")
    suspend fun getById(id: Long): AnnotationEntity?
}