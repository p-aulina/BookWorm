package com.example.bookworm.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookworm.domain.model.Book
import com.example.bookworm.ui.UiState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val booksState by viewModel.books.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = booksState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Empty -> {
                Text(text = "No books found — seeder may have failed")
            }

            is UiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.data) { book ->
                        BookDebugCard(book = book)
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun BookDebugCard(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Authors: ${book.author?.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Genres: ${book.genres.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Status: ${book.status}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Format: ${book.format}  |  Ownership: ${book.ownership}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Pages: ${book.pageCount}  |  Published: ${book.datePublished}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}