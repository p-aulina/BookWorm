package com.example.bookworm.ui.detail

import androidx.compose.runtime.snapshots.SnapshotId
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.data.local.entity.NoteEntity
import com.example.bookworm.data.local.entity.ReviewEntity
import com.example.bookworm.data.repository.BookRepository
import com.example.bookworm.data.repository.NoteRepository
import com.example.bookworm.data.repository.ReviewRepository
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus
import com.example.bookworm.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val noteRepository: NoteRepository,
    private val reviewRepository: ReviewRepository
): ViewModel() {
    private val _bookId = MutableStateFlow<String?>(null)

    val book: StateFlow<UiState<Book>> = _bookId
        .map { bookId ->
            if(bookId == null) return@map UiState.Loading
            bookRepository.observeBook(bookId)
                .map { book ->
                    if(book == null) UiState.Error("Book not found")
                    else UiState.Success(book)
                }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)
                .value
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    private val _book = MutableStateFlow<UiState<Book>>(UiState.Loading)
    val bookState: StateFlow<UiState<Book>> = _book.asStateFlow()

//    val notes: StateFlow<List<NoteEntity>> = _bookId
//        .map { bookId ->
//            bookId ?: return@map emptyList()
//            noteRepository.observeNotesForBook(bookId)
//        }
//        .map { emptyList<NoteEntity>() }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notesState: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    private val _review = MutableStateFlow<ReviewEntity?>(null)
    val reviewState: StateFlow<ReviewEntity?> = _review.asStateFlow()

    fun loadBook(bookId: String){
        _bookId.value = bookId
        viewModelScope.launch {
            bookRepository.observeBook(bookId).collect { book ->
                _book.value = if (book == null) UiState.Error("Book not found")
                else UiState.Success(book)
            }
        }
        viewModelScope.launch {
            noteRepository.observeNotesForBook(bookId).collect { notes ->
                _notes.value = notes
            }
        }
        viewModelScope.launch {
            reviewRepository.observeReviewForBook(bookId).collect { review ->
                _review.value = review
            }
        }
    }

    fun updateStatus(status: BookStatus){
        viewModelScope.launch {
            _bookId.value?.let { bookRepository.updateStatus(it, status) }
        }
    }

    fun toggleFormat(format: BookFormat){
        viewModelScope.launch {
            val bookId = _bookId.value?:return@launch
            val currentFormats = (bookState.value as? UiState.Success)
                ?.data?.ownedFormats ?: emptyList()
            if(format in currentFormats){
                bookRepository.removeFormat(bookId, format)
            } else {
                bookRepository.addFormat(bookId, format)
            }
        }
    }

    fun updateOwnership(ownership: OwnershipStatus){
        viewModelScope.launch {
            _bookId.value?.let { bookRepository.updateOwnership(it, ownership) }
        }
    }

    fun addNote(text: String, pageNr: Int?){
        viewModelScope.launch {
            _bookId.value?.let { noteRepository.addNote(it, text, pageNr) }
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun addReview(rating: Float, text: String?){
        viewModelScope.launch {
            _bookId.value?.let { reviewRepository.addReview(it, rating, text ?: "") }
        }
    }

    fun deleteReview(){
        viewModelScope.launch {
            _bookId.value?.let {
                reviewRepository.deleteReview(it)
            }
        }
    }

    fun deleteBook(onDeleted: () -> Unit){
        viewModelScope.launch {
            _bookId.value?.let {
                bookRepository.deleteBook(it)
                onDeleted()
            }
        }
    }
}