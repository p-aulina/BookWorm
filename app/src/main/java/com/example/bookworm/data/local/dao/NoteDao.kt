package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.bookworm.data.local.entity.NoteEntity
import com.example.bookworm.data.local.entity.NoteWithAnnotation
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("UPDATE note SET text = :text, pageNr = :pageNr WHERE noteId = :noteId")
    suspend fun updateNote(noteId: Long, text: String, pageNr: Int?)

    @Query("SELECT * FROM note WHERE bookId = :bookId ORDER BY timestamp DESC")
    fun observeNotesForBook(bookId: String): Flow<List<NoteEntity>>

    @Query("DELETE FROM note WHERE bookId = :bookId")
    suspend fun deleteAllForBook(bookId: String)

    @Transaction
    @Query("""
        SELECT * FROM note
        WHERE bookId = :bookId 
        ORDER BY timestamp DESC
    """)
    fun observeNotesWithAnnotation(bookId: String): Flow<List<NoteWithAnnotation>>

    @Transaction
    @Query("""
        SELECT * FROM note
        WHERE bookId = :bookId AND annotationId = :annotationId
        ORDER BY timestamp DESC
    """)
    fun observeNotesByAnnotation(
        bookId: String,
        annotationId: Long
    ): Flow<List<NoteWithAnnotation>>

    @Transaction
    @Query("""
        SELECT * FROM note
        WHERE bookId = :bookId AND annotationId IS NULL
        ORDER BY timestamp DESC
    """)
    fun observeUnannotatedNotes(bookId: String): Flow<List<NoteWithAnnotation>>
}