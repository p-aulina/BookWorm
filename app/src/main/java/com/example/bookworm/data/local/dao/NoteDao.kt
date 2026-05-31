package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookworm.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM note WHERE bookId = :bookId ORDER BY timestamp DESC")
    fun observeNotesForBook(bookId: String): Flow<List<NoteEntity>>

    @Query("DELETE FROM note WHERE bookId = :bookId")
    suspend fun deleteAllForBook(bookId: String)
}