package com.example.bookworm.data.repository

import androidx.room.Transaction
import com.example.bookworm.data.local.dao.AuthorDao
import com.example.bookworm.data.local.dao.BookAuthorDao
import com.example.bookworm.data.local.dao.BookDao
import com.example.bookworm.data.local.dao.BookGenreDao
import com.example.bookworm.data.local.dao.FormatDao
import com.example.bookworm.data.local.dao.GenreDao
import com.example.bookworm.data.local.entity.AuthorEntity
import com.example.bookworm.data.local.entity.BookAuthorCrossRef
import com.example.bookworm.data.local.entity.BookGenreCrossRef
import com.example.bookworm.data.local.entity.FormatEntity
import com.example.bookworm.data.local.entity.GenreEntity
import com.example.bookworm.data.mapper.BookMapper
import com.example.bookworm.data.mapper.RemoteBookMapper
import com.example.bookworm.data.remote.api.ApiService
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val genreDao: GenreDao,
    private val formatDao: FormatDao,
    private val bookAuthorDao: BookAuthorDao,
    private val bookGenreDao: BookGenreDao,
    private val booksApi: ApiService
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

        book.ownedFormats.forEach { format ->
            formatDao.insert(FormatEntity(bookId = book.bookId, format = format))
        }
    }

    suspend fun updateStatus(bookId: String, status: BookStatus) =
        bookDao.updateStatus(bookId, status)

    suspend fun deleteBook(bookId: String) =
        bookDao.delete(bookId)

    suspend fun searchBooks(
        query: String,
        maxResults: Int = 20,
        startIndex: Int = 0
    ): Result<List<Book>> = runCatching {
        val response = booksApi.searchBooks(
            query = query,
            maxResults = maxResults,
            startIndex = startIndex
        )
        RemoteBookMapper.toDomainList(response.items?:emptyList())
    }

    suspend fun getBookById(bookId: String): Result<Book> = runCatching {
        val dto = booksApi.getBookById(bookId)
        RemoteBookMapper.toDomain(dto)
    }

    suspend fun updateOwnership(bookId: String, ownership: OwnershipStatus) =
        bookDao.updateOwnership(bookId, ownership)

    suspend fun upsertFormat(user: FormatEntity) =
        formatDao.upsertFormat(user)

    fun observeBooksByOwnership(ownership: OwnershipStatus): Flow<List<Book>> =
        bookDao.observeBooksByOwnership(ownership)
            .map { BookMapper.toDomainList(it) }

    suspend fun addFormat(bookId: String, format: BookFormat) =
        formatDao.insert(FormatEntity(bookId = bookId, format = format))

    suspend fun removeFormat(bookId: String, format: BookFormat) =
        formatDao.delete(FormatEntity(bookId = bookId, format = format))

    fun observeFormatsForBook(bookId: String): Flow<List<BookFormat>> =
        formatDao.observeFormatsForBook(bookId)
}