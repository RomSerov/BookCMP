package org.example.bookapp.feature_book.presentation.previews.book_details

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.feature_book.presentation.book_details.BookDetailScreen
import org.example.bookapp.feature_book.presentation.book_details.BookDetailUiState

@Preview(showBackground = true)
@Composable
private fun TitledContentPreview() {
    MaterialTheme {
        BookDetailScreen(
            state = BookDetailUiState(
                isLoading = false,
                isFavourite = true,
                book = Book(
                    id = "1",
                    title = "Book 1",
                    imageUrl = "https://test.com",
                    authors = listOf("Test test"),
                    description = "Description",
                    languages = listOf("Русский", "English"),
                    firstPublishYear = "1955",
                    averageRating = 4.67854,
                    ratingCount = 5,
                    numPages = 100,
                    numEditions = 3
                )
            ),
            action = {}
        )
    }
}