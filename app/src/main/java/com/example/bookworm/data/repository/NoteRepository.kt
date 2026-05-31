package com.example.bookworm.data.repository

import com.example.bookworm.data.local.dao.NoteDao
import com.example.bookworm.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
){
    fun observeNotesForBook(bookId: String): Flow<List<NoteEntity>> =
        noteDao.observeNotesForBook(bookId)

    suspend fun addNote(bookId: String, text: String, pageNr: Int?){
        noteDao.insert(
            NoteEntity(
                bookId = bookId,
                text = text,
                pageNr = pageNr,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteNote(note: NoteEntity) =
        noteDao.delete(note)
}