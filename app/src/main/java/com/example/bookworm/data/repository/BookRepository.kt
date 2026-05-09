package com.example.bookworm.data.repository

import androidx.room.Transaction
import com.example.bookworm.data.local.dao.AuthorDao
import com.example.bookworm.data.local.dao.BookAuthorDao
import com.example.bookworm.data.local.dao.BookDao
import com.example.bookworm.data.local.dao.BookGenreDao
import com.example.bookworm.data.local.dao.GenreDao
import com.example.bookworm.data.local.entity.AuthorEntity
import com.example.bookworm.data.local.entity.BookAuthorCrossRef
import com.example.bookworm.data.local.entity.BookGenreCrossRef
import com.example.bookworm.data.local.entity.GenreEntity
import com.example.bookworm.data.mapper.BookMapper
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val genreDao: GenreDao,
    private val bookAuthorDao: BookAuthorDao,
    private val bookGenreDao: BookGenreDao
) {
    //Read
    fun observeAllBooks(): Flow<List<Book>> =
        bookDao.observeAllBooksWithDetails()
            .map { BookMapper.toDomainList(it) }

    fun observeBookByStatus(status: BookStatus): Flow<List<Book>> =
        bookDao.observeBooksWithDetailsByStatus(status)
            .map { BookMapper.toDomainList(it) }

    fun observeBook(bookId: String): Flow<Book?> =
        bookDao.observeBookWithDetails(bookId)
            .map { it?.let(BookMapper::toDomain) }

    suspend fun getBookCount(): Int = bookDao.getBookCount()

    //Write
    @Transaction
    suspend fun addBook(book: Book){
        bookDao.insertIfNotExists(BookMapper.toEntity(book))

        val authorIds = book.author.orEmpty().map { authorName ->
            authorDao.getByName(authorName)?.authorId
                ?: authorDao.insert(AuthorEntity(authorName = authorName))
        }
        bookAuthorDao.insertAll(
            authorIds.map { BookAuthorCrossRef(book.bookId, it) }
        )

        val genreIds = book.genres.map { genreName ->
            genreDao.getByName(genreName)?.genreId
                ?: genreDao.insert(GenreEntity(genreName = genreName))
        }
        bookGenreDao.insertAll(
            genreIds.map { BookGenreCrossRef(book.bookId, it) }
        )
    }

    suspend fun updateStatus(bookId: String, status: BookStatus) =
        bookDao.updateStatus(bookId, status)

    suspend fun deleteBook(bookId: String) =
        bookDao.delete(bookId)
}