package org.example.bookapp.feature_book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bookcmp.composeapp.generated.resources.Res
import bookcmp.composeapp.generated.resources.favorites
import bookcmp.composeapp.generated.resources.no_favorite_books
import bookcmp.composeapp.generated.resources.no_search_results
import bookcmp.composeapp.generated.resources.search_results
import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.feature_book.presentation.book_list.components.BookList
import org.example.bookapp.feature_book.presentation.book_list.components.BookSearchBar
import org.example.bookapp.core.presentation.DarkBlue
import org.example.bookapp.core.presentation.DesertWhite
import org.example.bookapp.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookListScreenRoot(
    viewModel: BookListViewModel,
    onBookClick: (Book) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is BookListEvent.OnNavigateBookDetail -> {
                    onBookClick(it.book)
                }
            }
        }
    }

    BookListScreen(
        state = state,
        action = {
            viewModel.onAction(it)
        }
    )
}

@Composable
fun BookListScreen(
    modifier: Modifier = Modifier,
    state: BookListUiState,
    action: (BookListAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState { 2 }
    val searchResultsListState = rememberLazyListState()
    val favoriteBooksListState = rememberLazyListState()

    LaunchedEffect(state.searchResult) {
        searchResultsListState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        action(BookListAction.OnTabSelected(index = pagerState.currentPage))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookSearchBar(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp),
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                action(BookListAction.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            }
        )

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    selectedTabIndex = state.selectedTabIndex,
                    containerColor = DesertWhite,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[state.selectedTabIndex]),
                            color = SandYellow
                        )
                    }
                ) {
                    Tab(
                        modifier = Modifier.weight(1f),
                        selected = state.selectedTabIndex == 0,
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                        onClick = {
                            action(BookListAction.OnTabSelected(index = 0))
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 12.dp),
                            text = stringResource(Res.string.search_results)
                        )
                    }

                    Tab(
                        modifier = Modifier.weight(1f),
                        selected = state.selectedTabIndex == 1,
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                        onClick = {
                            action(BookListAction.OnTabSelected(index = 1))
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 12.dp),
                            text = stringResource(Res.string.favorites)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = pagerState
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(page) {
                            0 -> {
                                if (state.isLoading) {
                                    CircularProgressIndicator()
                                } else {
                                    when {
                                        state.error != null -> {
                                            Text(
                                                text = state.error.asString(),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                        state.searchResult.isEmpty() -> {
                                            Text(
                                                text = stringResource(Res.string.no_search_results),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                        else -> {
                                            BookList(
                                                modifier = Modifier.fillMaxSize(),
                                                booksUi = state.searchResult,
                                                scrollState = searchResultsListState,
                                                onClickBook = {
                                                    action(BookListAction.OnClickBook(it))
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            1 -> {
                                if (state.favouriteBooks.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.no_favorite_books),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                    )
                                } else {
                                    BookList(
                                        modifier = Modifier.fillMaxSize(),
                                        booksUi = state.favouriteBooks,
                                        scrollState = favoriteBooksListState,
                                        onClickBook = {
                                            action(BookListAction.OnClickBook(it))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




















