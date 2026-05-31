package com.example.bookworm.data.repository

import android.icu.util.Calendar
import androidx.compose.ui.graphics.Path.Companion.combine
import androidx.compose.ui.text.style.TextDecoration.Companion.combine
import com.example.bookworm.data.local.dao.StatsDao
import com.example.bookworm.domain.model.FormatCount
import com.example.bookworm.domain.model.LengthBreakdown
import com.example.bookworm.domain.model.MonthlyCount
import com.example.bookworm.domain.model.NameCount
import com.example.bookworm.domain.model.OverviewStats
import com.example.bookworm.domain.model.Stats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val statsDao: StatsDao
){
    fun observeStats(): Flow<Stats>{
        val startOfYear = getStartOfYear()
        val twelveMonthsAgo = getTwelveMothsAgo()
        val monthNames = listOf(
            "Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec"
        )
        return combine(
            statsDao.observeTotalBooks(),
            statsDao.observeBooksRead(),
            statsDao.observeBooksReading(),
            statsDao.observeBooksTbr(),
            statsDao.observeBooksDnf(),
            statsDao.observeTotalPagesRead(),
            statsDao.observeBooksReadThisYear(startOfYear),
            statsDao.observeAverageRating(),
            statsDao.observeTopAuthors(),
            statsDao.observeTopGenres(),
            statsDao.observeFormatBreakdown(),
            statsDao.observeMonthlyProgress(twelveMonthsAgo),
            statsDao.observeShortBooks(),
            statsDao.observeMediumBooks(),
            statsDao.observeLongBooks(),
            statsDao.observeVeryLongBooks()
        ){ values ->
            val total = values[0] as Int
            val read = values[1] as Int
            val reading = values[2] as Int
            val tbr = values[3] as Int
            val dnf = values[4] as Int
            val pages = values[5] as Int
            val thisYear = values[6] as Int
            val avgRating = values[7] as Float
            @Suppress("UNCHECKED_CAST")
            val authors = values[8] as List<NameCount>
            @Suppress("UNCHECKED_CAST")
            val genres = values[9] as List<NameCount>
            @Suppress("UNCHECKED_CAST")
            val formats = values[10] as List<FormatCount>
            @Suppress("UNCHECKED_CAST")
            val monthlyRaw = values[11] as List<StatsDao.MonthlyRaw>
            val short = values[12] as Int
            val medium = values[13] as Int
            val long = values[14] as Int
            val veryLong = values[15] as Int

            val monthly = monthlyRaw.map { raw ->
                MonthlyCount(
                    month = monthNames.getOrElse(raw.month - 1) { "?" },
                    year = raw.year,
                    count = raw.count
                )
            }
            Stats(
                overview = OverviewStats(
                    totalBooks = total,
                    booksRead = read,
                    booksReading = reading,
                    booksTbr = tbr,
                    booksDnf = dnf,
                    totalPagesRead = pages,
                    booksReadThisYear = thisYear,
                    averageRating = avgRating
                ),
                topAuthors = authors,
                topGenres = genres,
                formatBreakdown = formats,
                monthlyProgress = monthly,
                lengthBreakdown = LengthBreakdown(
                    shortBooks = short,
                    mediumBooks = medium,
                    longBooks = long,
                    veryLongBooks = veryLong
                )
            )
        }
    }

    private fun getStartOfYear(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun getTwelveMothsAgo(): Long{
        return Calendar.getInstance().apply {
            add(Calendar.MONTH, -12)
        }.timeInMillis
    }
}