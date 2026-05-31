package com.example.bookworm.data.repository

import com.example.bookworm.data.local.dao.AnnotationDao
import com.example.bookworm.data.local.entity.AnnotationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnnotationRepository @Inject constructor(
    private val annotationDao: AnnotationDao
) {
    fun observeAll(): Flow<List<AnnotationEntity>> =
        annotationDao.observeAll()

    suspend fun createAnnotation(label: String, colorHex: String): Long =
        annotationDao.insert(AnnotationEntity(label = label, color = colorHex))

    suspend fun deleteAnnotation(annotation: AnnotationEntity) =
        annotationDao.delete(annotation)
}