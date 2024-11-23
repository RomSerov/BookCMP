package org.example.bookapp.feature_book.presentation.previews.book_list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.feature_book.presentation.book_list.BookListScreen
import org.example.bookapp.feature_book.presentation.book_list.BookListUiState
import org.example.bookapp.feature_book.presentation.book_list.components.BookListItem
import org.example.bookapp.feature_book.presentation.book_list.components.BookSearchBar

@Preview
@Composable
private fun BookSearchBarPreview() {
    BookSearchBar(
        modifier = Modifier.fillMaxWidth(),
        searchQuery = "f",
        onSearchQueryChange = {},
        onImeSearch = {}
    )
}

@Preview
@Composable
private fun BookListScreenPreview() {
    BookListScreen(
        state = BookListUiState(
            searchResult = (1..100).map {
                Book(
                    id = it.toString(),
                    title = "Book $it",
                    imageUrl = "https://test.com",
                    authors = listOf("Test Test"),
                    description = "Description $it",
                    languages = emptyList(),
                    firstPublishYear = "",
                    averageRating = 4.67854,
                    ratingCount = 5,
                    numPages = 100,
                    numEditions = 3
                )
            },
            selectedTabIndex = 0,
            isLoading = false
        ),
        action = {}
    )
}

@Preview
@Composable
private fun BookListItemPreview() {
    BookListItem(
        book = Book(
            id = "1",
            title = "Book 1",
            imageUrl = "https://test.com",
            authors = listOf("Test test"),
            description = "Description",
            languages = emptyList(),
            firstPublishYear = "1955",
            averageRating = 4.67854,
            ratingCount = 5,
            numPages = 100,
            numEditions = 3
        ),
        onClick = {}
    )
}