package com.example.bookworm.data.mapper

import com.example.bookworm.data.remote.dto.VolumeDto
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus

object RemoteBookMapper {
    fun toDomain(dto: VolumeDto): Book{
        val info = dto.volumeInfo
        val coverUrl = info.cover?.cover
            ?.replace("http://", "https://")
        return Book(
            bookId = dto.id,
            title = info.title,
            author = info.authors?:emptyList(),
            genres = info.genres?:emptyList(),
            description = info.description?:"",
            coverURL = coverUrl,
            pageCount = info.pageCount?:0,
            datePublished = info.datePublished?:"",
            language = info.language?:"",
            //user fields
            status = BookStatus.TBR,
            format = BookFormat.PHYSICAL,
            ownership = OwnershipStatus.WISHLIST,
            dateAddedToLibrary = System.currentTimeMillis(),
            dateStarted = null,
            dateFinished = null,
            dateLastUpdate = System.currentTimeMillis(),
            pageProgress = 0
        )
    }

    fun toDomainList(dto: List<VolumeDto>): List<Book> = dto.map { toDomain(it) }
}