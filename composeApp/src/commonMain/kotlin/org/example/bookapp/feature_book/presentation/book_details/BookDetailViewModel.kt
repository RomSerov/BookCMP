package org.example.bookapp.feature_book.presentation.book_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.bookapp.common.Route
import org.example.bookapp.core.domain.onSuccess
import org.example.bookapp.feature_book.domain.repository.BookRepository

class BookDetailViewModel(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id

    private val _state = MutableStateFlow(BookDetailUiState())
    val state = _state
        .onStart {
            fetchBookDescription()
            observeFavouriteStatus()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    private val _event = Channel<BookDetailEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: BookDetailAction) {
        when(action) {
            BookDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    _event.send(BookDetailEvent.NavigateBack)
                }
            }
            BookDetailAction.OnFavouriteClick -> {
                viewModelScope.launch {
                    if (state.value.isFavourite) {
                        bookRepository.deleteFromFavourite(bookId)
                    } else {
                        state.value.book?.let {
                            bookRepository.markAsFavourite(book = it)
                        }
                    }
                }
            }
            is BookDetailAction.OnSelectedBookChange -> {
                _state.update { it.copy(book = action.book) }
            }
        }
    }

    private fun observeFavouriteStatus() {
        bookRepository
            .isBookFavourite(id = bookId)
            .onEach { isFavourite ->
                _state.update { it.copy(isFavourite = isFavourite) }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchBookDescription() {
        viewModelScope.launch {
            bookRepository
                .getBookDescription(bookId = bookId)
                .onSuccess { desc ->
                    _state.update { it.copy(
                        book = it.book?.copy(description = desc),
                        isLoading = false
                    ) }
                }
        }
    }
}