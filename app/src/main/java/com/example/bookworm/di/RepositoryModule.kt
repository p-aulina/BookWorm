package com.example.bookworm.di

import com.example.bookworm.data.local.dao.AuthorDao
import com.example.bookworm.data.local.dao.BookAuthorDao
import com.example.bookworm.data.local.dao.BookDao
import com.example.bookworm.data.local.dao.BookGenreDao
import com.example.bookworm.data.local.dao.FormatDao
import com.example.bookworm.data.local.dao.GenreDao
import com.example.bookworm.data.local.dao.NoteDao
import com.example.bookworm.data.local.dao.ReviewDao
import com.example.bookworm.data.remote.api.ApiService
import com.example.bookworm.data.repository.BookRepository
import com.example.bookworm.data.repository.NoteRepository
import com.example.bookworm.data.repository.ReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesBookRepository(
        bookDao: BookDao,
        authorDao: AuthorDao,
        genreDao: GenreDao,
        formatDao: FormatDao,
        bookAuthorDao: BookAuthorDao,
        bookGenreDao: BookGenreDao,
        booksApi: ApiService
        ): BookRepository {
        return BookRepository(bookDao, authorDao, genreDao, formatDao, bookAuthorDao, bookGenreDao, booksApi)
    }

    @Provides
    @Singleton
    fun providesNoteRepository(noteDao: NoteDao): NoteRepository =
        NoteRepository(noteDao)

    @Provides
    @Singleton
    fun providesReviewRepository(reviewDao: ReviewDao): ReviewRepository =
        ReviewRepository(reviewDao)
}