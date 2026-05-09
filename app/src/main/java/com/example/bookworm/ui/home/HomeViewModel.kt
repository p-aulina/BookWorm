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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val seeder: DatabaseSeeder
): ViewModel() {
    private val _books = MutableStateFlow<UiState<List<Book>>>(UiState.Loading)
    val books: StateFlow<UiState<List<Book>>> = _books.asStateFlow()
    private val _currentStatus = MutableStateFlow<BookStatus?>(null)
    val currentStatus: StateFlow<BookStatus?> = _currentStatus.asStateFlow()

    init {
        seedIfEmpty()
        observeBooks()
    }

    private fun seedIfEmpty(){
        viewModelScope.launch {
            val count = bookRepository.getBookCount()
            if(count == 0){
                seeder.seed()
            }
        }
    }

    private fun observeBooks(){
        viewModelScope.launch {
            _currentStatus.collect { status ->
                val flow = if (status == null){
                    bookRepository.observeAllBooks()
                } else {
                    bookRepository.observeBookByStatus(status)
                }

                flow.collect { books ->
                    _books.value = if (books.isEmpty()){
                        UiState.Empty
                    } else {
                        UiState.Success(books)
                    }
                }
            }
        }
    }


}