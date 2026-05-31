package com.example.bookworm.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.bookworm.data.local.entity.NoteEntity
import com.example.bookworm.data.local.entity.ReviewEntity
import com.example.bookworm.domain.model.Book
import com.example.bookworm.domain.model.BookFormat
import com.example.bookworm.domain.model.BookStatus
import com.example.bookworm.domain.model.OwnershipStatus
import com.example.bookworm.ui.UiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookDetailModel = hiltViewModel()
) {
    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    val bookState by viewModel.bookState.collectAsStateWithLifecycle()
    val notes by viewModel.notesState.collectAsStateWithLifecycle()
    val review by viewModel.reviewState.collectAsStateWithLifecycle()

    var showNoteDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BookDetail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {showDeleteConfirm = true}) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete book",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when(val state = bookState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {Text(state.message, color = MaterialTheme.colorScheme.error)}
            }
            is UiState.Success -> {
                val book = state.data
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item { BookHeader(book = book) }
                    item {
                        BookMetaSection(
                            book = book,
                            onStatusChange = viewModel::updateStatus,
                            onFormatToggle = viewModel::toggleFormat,
                            onOwnershipChange = viewModel::updateOwnership
                        )
                    }
                    if(!book.description.isNullOrBlank()){
                        item { DescriptionSection(description = book.description) }
                    }

                    item {
                        ReviewSection(
                            review = review,
                            onAddReviewClick = { showReviewDialog = true },
                            onDeleteReview = viewModel::deleteReview
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Reading Notes",
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(onClick = { showNoteDialog = true }) {
                                Text("+ Add Note")
                            }
                        }
                    }
                    if (notes.isEmpty()) {
                        item{
                            Text(
                                text = "No notes yet. Add one while reading!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        items(notes, key = { it.noteId }) { note ->
                            NoteCard(
                                note = note,
                                onDelete = { viewModel.deleteNote(note) }
                            )
                        }
                    }
                }
            }
            else -> Unit
        }
    }
    if (showNoteDialog){
        AddNoteDialog(
            onDismiss = { showNoteDialog = false },
            onConfirm ={ text, page ->
                viewModel.addNote(text, page)
                showNoteDialog = false
            }
        )
    }
    if(showReviewDialog){
        AddReviewDialog(
            onDismiss = { showReviewDialog = false },
            onConfirm = { rating, text ->
                viewModel.addReview(rating, text)
                showReviewDialog = false
            }
        )
    }
    if(showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Book") },
            text = { Text("Are you sure? This will also delete all notes and your review.")},
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteBook(onBack)
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }){
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun BookHeader(book: Book){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        AsyncImage(
            model = book.coverURL,
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 150.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.weight(1f)){
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.author?.joinToString(", ").orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if(book.datePublished != null){
                Text(
                    text = book.datePublished,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if(book.pageCount > 0){
                Text(
                    text = "${book.pageCount} pages",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (book.genres?.isNotEmpty() == true){
                Text(
                    text = book.genres.take(3).joinToString(" · " ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BookMetaSection(
    book: Book,
    onStatusChange: (BookStatus) -> Unit,
    onFormatToggle: (BookFormat) -> Unit,
    onOwnershipChange: (OwnershipStatus) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider()
        PickerRow(
            label = "Status",
            options = BookStatus.entries.map { it.name },
            selected = book.status.name,
            onSelect = { name ->
                onStatusChange(BookStatus.valueOf(name))
            }
        )
        Column {
            Text(
                text = "Format",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BookFormat.entries.forEach { format ->
                    FilterChip(
                        selected = format in book.ownedFormats,
                        onClick = {onFormatToggle(format)},
                        label = {
                            Text(
                                format.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
        PickerRow(
            label = "Ownership",
            options = OwnershipStatus.entries.map { it.name },
            selected = book.ownership.name,
            onSelect = { name ->
                onOwnershipChange(OwnershipStatus.valueOf(name))
            }
        )
        HorizontalDivider()
    }
}

@Composable
private fun PickerRow(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
){
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selected,
                    onClick = {onSelect(option)},
                    label = {
                        Text(
                            option.replace("_", " ")
                                .lowercase()
                                .replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }
    }
}
@Composable
private fun DescriptionSection(description: String){
    var expanded by remember {mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Text("About", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            maxLines = if(expanded) Int.MAX_VALUE else 4,
            overflow = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis
        )
        TextButton(onClick = {expanded = !expanded}) {
            Text(if (expanded) "Show less" else "Read more")
        }
    }
}

@Composable
private fun ReviewSection(
    review: ReviewEntity?,
    onAddReviewClick: () -> Unit,
    onDeleteReview: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Review", style = MaterialTheme.typography.titleMedium)
            if(review == null){
                TextButton(onClick = onAddReviewClick) { Text("+ Add Review") }
            } else {
                IconButton(onClick = onDeleteReview){
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete review",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        if(review != null){
            Row {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if(index < review.rating) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            if(!review.text.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = review.text,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "Written ${formatDate(review.date)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "No review yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NoteCard(
    note: NoteEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if(note.pageNr != null){
                    Text(
                        text = "Page ${note.pageNr}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = note.text,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatDate(note.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete note",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AddNoteDialog(
    onDismiss: () -> Unit,
    onConfirm: (text: String, page: Int?) -> Unit
){
    var text by remember { mutableStateOf("") }
    var pageText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("AddNote") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {text = it},
                    label = {Text("Note")},
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pageText,
                    onValueChange = {pageText = it.filter { c -> c.isDigit() }},
                    label = {Text("Page number (optional)")},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(text.isNotBlank()){
                        onConfirm(text.trim(), pageText.toIntOrNull())
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AddReviewDialog(
    onDismiss: () -> Unit,
    onConfirm: (rating: Float, text: String?) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Write a Review")},
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Rating", style = MaterialTheme.typography.labelMedium)
                Row {
                    repeat(5){ index ->
                        IconButton(onClick = {rating = index + 1}) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "${index + 1} stars",
                                tint = if(index < rating) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Your review") },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(rating > 0) {
                        onConfirm(rating.toFloat(), text.trim())
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun formatDate(timestamp: Long): String{
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}