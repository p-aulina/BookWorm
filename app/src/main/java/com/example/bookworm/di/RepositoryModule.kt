package com.example.bookworm.di

import com.example.bookworm.data.local.dao.AuthorDao
import com.example.bookworm.data.local.dao.BookAuthorDao
import com.example.bookworm.data.local.dao.BookDao
import com.example.bookworm.data.local.dao.BookGenreDao
import com.example.bookworm.data.local.dao.GenreDao
import com.example.bookworm.data.remote.api.ApiService
import com.example.bookworm.data.repository.BookRepository
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
        bookAuthorDao: BookAuthorDao,
        bookGenreDao: BookGenreDao,
        booksApi: ApiService
        ): BookRepository {
        return BookRepository(bookDao, authorDao, genreDao, bookAuthorDao, bookGenreDao, booksApi)
    }
}