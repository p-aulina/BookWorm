package com.example.bookworm.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.data.repository.BookRepository
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.ui.UiState
import com.example.bookworm.utils.DatabaseSeeder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val seeder: DatabaseSeeder
): ViewModel() {

    val currentlyReading: StateFlow<List<Book>> = bookRepository
        .observeBookByStatus(BookStatus.READING)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentlyAdded: StateFlow<List<Book>> = bookRepository
        .observeAllBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        seedIfEmpty()
    }

    private fun seedIfEmpty() {
        viewModelScope.launch {
            if (bookRepository.getBookCount() == 0) seeder.seed()
        }
    }
}