package com.example.bookworm.di

import androidx.room.Room
import com.example.bookworm.data.local.BookWormDatabase
import android.content.Context
import com.example.bookworm.data.local.dao.AnnotationDao
import com.example.bookworm.data.local.dao.AuthorDao
import com.example.bookworm.data.local.dao.BookAuthorDao
import com.example.bookworm.data.local.dao.BookDao
import com.example.bookworm.data.local.dao.BookGenreDao
import com.example.bookworm.data.local.dao.FormatDao
import com.example.bookworm.data.local.dao.GenreDao
import com.example.bookworm.data.local.dao.NoteDao
import com.example.bookworm.data.local.dao.ReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookWormDatabase {
        return Room.databaseBuilder(
            context,
            BookWormDatabase::class.java,
            "bookworm.db"
        ).build()
    }

    @Provides fun provideBookDao(db: BookWormDatabase): BookDao = db.bookDao()
    @Provides fun provideAuthorDao(db: BookWormDatabase): AuthorDao = db.authorDao()
    @Provides fun provideGenreDao(db: BookWormDatabase): GenreDao = db.genreDao()
    @Provides fun provideBookAuthorDao(db: BookWormDatabase): BookAuthorDao = db.bookAuthorDao()
    @Provides fun provideBookGenreDao(db: BookWormDatabase): BookGenreDao = db.bookGenreDao()
    @Provides fun provideFormatDao(db: BookWormDatabase): FormatDao = db.formatDao()
    @Provides fun providesNoteDao(db: BookWormDatabase): NoteDao = db.noteDao()
    @Provides fun providesReviewDao(db: BookWormDatabase): ReviewDao = db.reviewDao()
    @Provides fun providesAnnotationDao(db: BookWormDatabase): AnnotationDao = db.annotationDao()
}