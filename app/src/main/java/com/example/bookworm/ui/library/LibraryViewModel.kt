package com.example.bookworm.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.data.repository.BookRepository
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus
import com.example.bookworm.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel(){
    private val _selectedShelf = MutableStateFlow(ShelfTab.ALL)
    val selectedShelf: StateFlow<ShelfTab> = _selectedShelf.asStateFlow()

    val books: StateFlow<UiState<List<Book>>> = _selectedShelf
        .flatMapLatest { shelf ->
            when(shelf){
                ShelfTab.ALL -> bookRepository.observeAllBooks()
                ShelfTab.READING -> bookRepository.observeBookByStatus(BookStatus.READING)
                ShelfTab.TO_BE_READ -> bookRepository.observeBookByStatus(BookStatus.TBR)
                ShelfTab.READ -> bookRepository.observeBookByStatus(BookStatus.FINISHED)
                ShelfTab.OWNED -> bookRepository.observeBooksByOwnership(OwnershipStatus.OWNED)
                ShelfTab.BORROWED -> bookRepository.observeBooksByOwnership(OwnershipStatus.BORROWED)
            }.map { bookList ->
                if(bookList.isEmpty()) UiState.Empty
                else UiState.Success(bookList)
            }
        }
        .let { flow ->
            val state = MutableStateFlow<UiState<List<Book>>>(UiState.Loading)
            viewModelScope.launch {
                flow.collect { state.value = it }
            }
            state.asStateFlow()
        }

    fun selectShelf(shelf: ShelfTab){
        _selectedShelf.value = shelf
    }

    fun deleteBook(bookId: String){
        viewModelScope.launch {
            bookRepository.deleteBook(bookId)
        }
    }

    fun updateStatus(bookId: String, newStatus: BookStatus){
        viewModelScope.launch {
            bookRepository.updateStatus(bookId, newStatus)
        }
    }
}