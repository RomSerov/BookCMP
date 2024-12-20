package org.example.bookapp.feature_book.presentation.book_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.bookapp.feature_book.domain.model.Book

@Composable
fun BookList(
    modifier: Modifier = Modifier,
    booksUi: List<Book>,
    scrollState: LazyListState = rememberLazyListState(),
    onClickBook: (Book) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = booksUi,
            key = { it.id }
        ) { book ->
            BookListItem(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                book = book,
                onClick = {
                    onClickBook(book)
                }
            )
        }
    }
}