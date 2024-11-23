package org.example.bookapp.feature_book.presentation.book_details

import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.feature_book.presentation.book_list.BookListAction

data class BookDetailUiState(
    val isLoading: Boolean = false,
    val isFavourite: Boolean = false,
    val book: Book? = null
)

sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavouriteClick: BookDetailAction
    data class OnSelectedBookChange(val book: Book): BookDetailAction
}

sealed interface BookDetailEvent {

    data object NavigateBack: BookDetailEvent
}

