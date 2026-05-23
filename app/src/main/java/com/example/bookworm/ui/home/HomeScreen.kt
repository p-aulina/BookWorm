package com.example.bookworm.ui.home

import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.bookworm.domain.model.Book

@Composable
fun HomeScreen(
    onBookClick: (String) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentlyReading by viewModel.currentlyReading.collectAsStateWithLifecycle()
    val recentlyAdded by viewModel.recentlyAdded.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                Text(
                    text = greeting(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = if(currentlyReading.isEmpty()) "Pick up a book today"
                        else "${currentlyReading.size} book${if(currentlyReading.size > 1) "s" else ""} in progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if(currentlyReading.isNotEmpty()){
            item {
                SectionHeader(title = "Currently Reading", showSeeAll = false)
            }
            items(currentlyReading){book ->
                CurrentlyReadingCard(
                    book = book,
                    onClick = {onBookClick(book.bookId)},
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
        if(recentlyAdded.isNotEmpty()){
            item {
                SectionHeader(
                    title = "Recently Added",
                    showSeeAll = true,
                    onSeeAllClick = onSeeAllClick
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentlyAdded.take(10)) { book ->
                        SmallBookCover(
                            book = book,
                            onClick = { onBookClick(book.bookId) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    showSeeAll: Boolean,
    onSeeAllClick: (() -> Unit)? = null
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if(showSeeAll && onSeeAllClick != null){
            TextButton(onClick = onSeeAllClick) { Text("See all") }
        }
    }
}

@Composable
private fun CurrentlyReadingCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ){
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.coverURL,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 56.dp, height = 80.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
private fun SmallBookCover(book: Book, onClick: () -> Unit){
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = book.coverURL,
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 80.dp, height = 110.dp)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.title,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun greeting(): String{
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..21 -> "Good evening"
        else -> "Reading late?"
    }
}