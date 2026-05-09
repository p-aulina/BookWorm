package com.example.bookworm.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.BuildConfig
import com.example.bookworm.data.repository.BookRepository
import com.example.bookworm.domain.model.Book
import com.example.bookworm.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _searchResults = MutableStateFlow<UiState<List<Book>>>(UiState.Empty)
    val searchResults: StateFlow<UiState<List<Book>>> = _searchResults.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _addedBookIds = MutableStateFlow<Set<String>>(emptySet())
    val addedBookIds: StateFlow<Set<String>> = _addedBookIds.asStateFlow()

    init{
        Log.d("API_KEY_CHECK", BuildConfig.API_KEY)
        observeQuery()
    }

    private fun observeQuery(){
        viewModelScope.launch {
            _query
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.length >= 2}
                .collect { query ->
                    search(query)
                }
        }
    }

    fun onQueryChanged(newQuery: String){
        _query.value = newQuery
        if(newQuery.isBlank()){
            _searchResults.value = UiState.Empty
        }
    }

    private suspend fun search(query: String){
        _searchResults.value = UiState.Loading
        bookRepository.searchBooks(query)
            .onSuccess { books ->
                _searchResults.value = if(books.isEmpty()) UiState.Empty
                                       else UiState.Success(books)
            }
            .onFailure { error ->
                _searchResults.value = UiState.Error(
                    error.message?:"Search failed"
                )
            }
    }

    fun addBookToLibrary(book: Book){
        viewModelScope.launch {
            bookRepository.addBook(book)
            _addedBookIds.value = _addedBookIds.value + book.bookId
        }
    }
}