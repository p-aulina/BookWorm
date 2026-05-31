package com.example.bookworm.domain.model

data class Stats(
    val overview: OverviewStats,
    val topAuthors: List<NameCount>,
    val topGenres: List<NameCount>,
    val formatBreakdown: List<FormatCount>,
    val monthlyProgress: List<MonthlyCount>,
    val lengthBreakdown: LengthBreakdown
)

data class OverviewStats(
    val totalBooks: Int,
    val booksRead: Int,
    val booksReading: Int,
    val booksTbr: Int,
    val booksDnf: Int,
    val totalPagesRead: Int,
    val booksReadThisYear: Int,
    val averageRating: Float
)

data class NameCount(
    val name: String,
    val count: Int
)

data class FormatCount(
    val format: String,
    val count: Int
)

data class MonthlyCount(
    val month: String,
    val year: Int,
    val count: Int
)

data class LengthBreakdown(
    val shortBooks: Int,      // < 200 pages
    val mediumBooks: Int,     // 200–400 pages
    val longBooks: Int,       // 400–600 pages
    val veryLongBooks: Int    // 600+ pages
)