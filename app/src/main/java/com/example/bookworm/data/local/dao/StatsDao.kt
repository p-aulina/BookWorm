package com.example.bookworm.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bookworm.domain.model.FormatCount
import com.example.bookworm.domain.model.NameCount
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {
    @Query("SELECT COUNT(*) FROM books")
    fun observeTotalBooks(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE status = 'FINISHED'")
    fun observeBooksRead(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE status = 'READING'")
    fun observeBooksReading(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE status = 'TBR'")
    fun observeBooksTbr(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE status = 'DNF'")
    fun observeBooksDnf(): Flow<Int>

    @Query("""
        SELECT COALESCE(SUM(pageCount), 0)
        FROM books
        WHERE status = 'FINISHED'
    """)
    fun observeTotalPagesRead(): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM books
        WHERE status = 'FINISHED'
        AND dateFinished >= :startOfYear
    """)
    fun observeBooksReadThisYear(startOfYear: Long): Flow<Int>

    @Query("""
        SELECT AVG(r.rating)
        FROM review r INNER JOIN books b
        ON b.bookId = r.bookId
        WHERE b.status = 'FINISHED' AND r.rating IS NOT NULL
    """)
    fun observeAverageRating(): Flow<Float>

    @Query("""
        SELECT authorName as name, COUNT(*) as count
        FROM authors a
        INNER JOIN book_authors ba ON a.authorId = ba.authorId
        INNER JOIN books b ON ba.bookId = b.bookId
        WHERE b.status = 'FINISHED'
        GROUP BY a.authorId
        ORDER BY count DESC
        LIMIT :limit
    """)
    fun observeTopAuthors(limit: Int = 10): Flow<List<NameCount>>

    @Query("""
        SELECT genreName as name, COUNT(*) as count
        FROM genres g
        INNER JOIN book_genres bg ON g.genreId = bg.genreId
        INNER JOIN books b ON bg.bookId = b.bookId
        WHERE b.status = 'FINISHED'
        GROUP BY g.genreId
        ORDER BY count DESC
        LIMIT :limit
    """)
    fun observeTopGenres(limit: Int = 10): Flow<List<NameCount>>

    @Query("""
        SELECT f.format, COUNT(DISTINCT f.bookId) AS count
        FROM book_format f
        INNER JOIN books b ON f.bookId = b.bookId
        GROUP BY f.format
        ORDER BY count DESC
    """)
    fun observeFormatBreakdown(): Flow<List<FormatCount>>

    @Query("""
        SELECT 
            CAST(strftime('%m', datetime(dateFinished / 1000, 'unixepoch')) AS INTEGER) AS month,
            CAST(strftime('%Y', datetime(dateFinished / 1000, 'unixepoch')) AS INTEGER) AS year,
            COUNT(*) AS count
        FROM books
        WHERE status = 'FINISHED'
          AND dateFinished >= :since
        GROUP BY year, month
        ORDER BY year ASC, month ASC
    """)
    fun observeMonthlyProgress(since: Long): Flow<List<MonthlyRaw>>

    data class MonthlyRaw(
        val month: Int,
        val year: Int,
        val count: Int
    )

    @Query("""
        SELECT COUNT(*) FROM books
        WHERE status = 'FINISHED' AND pageCount > 0 AND pageCount < 200
    """)
    fun observeShortBooks(): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM books
        WHERE status = 'FINISHED' AND pageCount >= 200 AND pageCount < 400
    """)
    fun observeMediumBooks(): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM books
        WHERE status = 'FINISHED' AND pageCount >= 400 AND pageCount < 600
    """)
    fun observeLongBooks(): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM books
        WHERE status = 'FINISHED' AND pageCount >= 600
    """)
    fun observeVeryLongBooks(): Flow<Int>
}