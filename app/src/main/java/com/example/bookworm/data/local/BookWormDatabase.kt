package com.example.bookworm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookworm.data.local.converters.Converters
import com.example.bookworm.data.local.dao.AuthorDao
import com.example.bookworm.data.local.dao.BookAuthorDao
import com.example.bookworm.data.local.dao.BookDao
import com.example.bookworm.data.local.dao.BookGenreDao
import com.example.bookworm.data.local.dao.FormatDao
import com.example.bookworm.data.local.dao.GenreDao
import com.example.bookworm.data.local.entity.AuthorEntity
import com.example.bookworm.data.local.entity.BookAuthorCrossRef
import com.example.bookworm.data.local.entity.BookEntity
import com.example.bookworm.data.local.entity.BookGenreCrossRef
import com.example.bookworm.data.local.entity.FormatEntity
import com.example.bookworm.data.local.entity.GenreEntity

@Database(
    entities = [
        BookEntity::class,
        AuthorEntity::class,
        GenreEntity::class,
        BookAuthorCrossRef::class,
        BookGenreCrossRef::class,
        FormatEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BookWormDatabase : RoomDatabase(){
    abstract fun bookDao(): BookDao
    abstract fun authorDao(): AuthorDao
    abstract fun genreDao(): GenreDao
    abstract fun bookAuthorDao(): BookAuthorDao
    abstract fun bookGenreDao(): BookGenreDao
    abstract fun formatDao(): FormatDao
}