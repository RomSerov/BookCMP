package org.example.bookapp.common

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.example.bookapp.feature_book.presentation.book_details.BookDetailAction
import org.example.bookapp.feature_book.presentation.book_details.BookDetailScreenRoot
import org.example.bookapp.feature_book.presentation.book_details.BookDetailViewModel
import org.example.bookapp.feature_book.presentation.book_list.BookListScreenRoot
import org.example.bookapp.feature_book.presentation.book_list.BookListViewModel
import org.example.bookapp.feature_book.presentation.common.SelectedBookViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Route.BookGraph
        ) {
            navigation<Route.BookGraph>(
                startDestination = Route.BookList
            ) {
                composable<Route.BookList>(
                    exitTransition = { slideOutHorizontally() },
                    popEnterTransition = { slideInHorizontally() }
                ) {
                    val viewModel = koinViewModel<BookListViewModel>()
                    val sharedViewModel = it.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    LaunchedEffect(Unit) {
                        sharedViewModel.onSelectBook(null)
                    }

                    BookListScreenRoot(
                        viewModel = viewModel,
                        onBookClick = { book ->
                            sharedViewModel.onSelectBook(book = book)
                            navController.navigate(
                                Route.BookDetail(id = book.id)
                            )
                        }
                    )
                }

                composable<Route.BookDetail>(
                    enterTransition = { slideInHorizontally { initialOffset -> initialOffset } },
                    exitTransition = { slideOutHorizontally { initialOffset -> initialOffset } }
                ) {
                    val viewModel = koinViewModel<BookDetailViewModel>()
                    val sharedViewModel = it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    val selectedBook by sharedViewModel.selectedBook.collectAsStateWithLifecycle()

                    LaunchedEffect(selectedBook) {
                        selectedBook?.let { book ->
                            viewModel.onAction(BookDetailAction.OnSelectedBookChange(book))
                        }
                    }

                    BookDetailScreenRoot(
                        viewModel = viewModel,
                        onBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}