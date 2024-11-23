package org.example.bookapp.feature_book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.bookapp.core.domain.onError
import org.example.bookapp.core.domain.onSuccess
import org.example.bookapp.core.presentation.toUiText
import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.feature_book.domain.repository.BookRepository

class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private var cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null
    private var observeFavouriteJob: Job? = null

    private val _state = MutableStateFlow(BookListUiState())
    val state = _state
        .onStart {
            if (cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
            observeFavoriteBooks()
        }.stateIn(
           viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    private val _event = Channel<BookListEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: BookListAction) {
        when(action) {
            is BookListAction.OnClickBook -> {
                viewModelScope.launch {
                    _event.send(BookListEvent.OnNavigateBookDetail(
                        book = action.book
                    ))
                }
            }
            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }

            is BookListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    private fun observeFavoriteBooks() {
        observeFavouriteJob?.cancel()
        observeFavouriteJob = bookRepository
            .getFavouriteBooks()
            .onEach { books ->
                _state.update {
                    it.copy(favouriteBooks = books)
                }
            }.launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach {  query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                error = null,
                                searchResult = cachedBooks
                            )
                        }
                    }
                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {

        _state.update { it.copy(isLoading = true) }

        bookRepository
            .searchBooks(query = query)
            .onSuccess { books ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        searchResult = books
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        searchResult = emptyList(),
                        error = error.toUiText()
                    )
                }
            }
    }
}