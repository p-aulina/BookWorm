package com.example.bookworm.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookworm.ui.UiState

@Composable
fun LibraryScreen(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel()
){
    val selectedShelf by viewModel.selectedShelf.collectAsStateWithLifecycle()
    val bookState by viewModel.books.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        FlowRow (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ShelfTab.entries.forEach { shelf ->
                FilterChip(
                    selected = shelf == selectedShelf,
                    onClick = { viewModel.selectShelf(shelf) },
                    label = { Text(shelf.label) }
                )
            }
        }
        Text(
            text = selectedShelf.label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            when(val state = bookState){
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Empty -> {
                    Text(
                        text = "No books on the ${selectedShelf.label} shelf yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                is UiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.data,
                            key = { it.bookId }
                        ) { book ->
                            BookCard(
                                book = book,
                                onClick = { onBookClick(book.bookId)},
                                onStatusChange = { newStatus ->
                                    viewModel.updateStatus(book.bookId, newStatus)
                                },
                                onDeleteClick = {
                                    viewModel.deleteBook(book.bookId)
                                }
                            )
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}