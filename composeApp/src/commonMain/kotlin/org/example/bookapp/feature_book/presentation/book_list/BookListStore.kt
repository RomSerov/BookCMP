package org.example.bookapp.feature_book.presentation.book_list

import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.core.presentation.UiText

data class BookListUiState(
    val searchQuery: String = "Kotlin",
    val searchResult: List<Book> = emptyList(),
    val favouriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val error: UiText? = null
)

sealed interface BookListAction {
    data class OnSearchQueryChange(val query: String): BookListAction
    data class OnClickBook(val book: Book): BookListAction
    data class OnTabSelected(val index: Int): BookListAction
}

sealed interface BookListEvent {

    data class OnNavigateBookDetail(val book: Book): BookListEvent
}