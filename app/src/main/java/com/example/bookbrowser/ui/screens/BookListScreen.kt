package com.example.bookbrowser.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookbrowser.config.BuildConfigHelper
import com.example.bookbrowser.config.DebugSettingsStore
import coil.compose.AsyncImage
import com.example.bookbrowser.data.model.BookItem
import com.example.bookbrowser.ui.viewmodels.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    onBookClick: (String) -> Unit,
    onOpenDebugSettings: () -> Unit,
    onOpenFeature: () -> Unit,
    viewModel: BookViewModel = viewModel()
) {
    val books by viewModel.books
    val isLoading by viewModel.isLoading
    val currentQuery by DebugSettingsStore.bookQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Popular Books") },
                actions = {
                    TextButton(onClick = onOpenFeature) {
                        Text(if (BuildConfigHelper.isPremium) "Premium" else "Feature")
                    }
                    if (BuildConfigHelper.isDebug) {
                        TextButton(onClick = onOpenDebugSettings) {
                            Text("Debug")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Text(
                text = "Query: $currentQuery",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books) { book ->
                    BookCard(book = book, onBookClick = onBookClick)
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: BookItem,
    onBookClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onBookClick(book.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 70.dp, height = 100.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = book.volumeInfo.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                val authors = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author"
                Text(
                    text = authors,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}
