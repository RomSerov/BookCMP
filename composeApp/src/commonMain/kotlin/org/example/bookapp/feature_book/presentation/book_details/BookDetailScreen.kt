package org.example.bookapp.feature_book.presentation.book_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bookcmp.composeapp.generated.resources.Res
import bookcmp.composeapp.generated.resources.description_unavailable
import bookcmp.composeapp.generated.resources.languages
import bookcmp.composeapp.generated.resources.pages
import bookcmp.composeapp.generated.resources.rating
import bookcmp.composeapp.generated.resources.synopsis
import org.example.bookapp.core.presentation.SandYellow
import org.example.bookapp.feature_book.presentation.book_details.components.BlurredImageBackground
import org.example.bookapp.feature_book.presentation.book_details.components.BookChip
import org.example.bookapp.feature_book.presentation.book_details.components.ChipSize
import org.example.bookapp.feature_book.presentation.book_details.components.TitledContent
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                BookDetailEvent.NavigateBack -> {
                    onBack()
                }
            }
        }
    }

    BookDetailScreen(
        state = state,
        action = {
            viewModel.onAction(it)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    state: BookDetailUiState,
    action: (BookDetailAction) -> Unit
) {
    BlurredImageBackground(
        modifier = Modifier.fillMaxSize(),
        imageUrl = state.book?.imageUrl,
        isFavourite = state.isFavourite,
        action = action
    ) {
        if(state.book != null) {
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp,
                        horizontal = 24.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = state.book.authors.joinToString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.book.averageRating?.let { 
                        TitledContent(
                            title = stringResource(Res.string.rating)
                        ) {
                            BookChip {
                                Text(
                                    text = "${round(it * 10) / 10.0}"
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SandYellow
                                )
                            }
                        }
                    }

                    state.book.numPages?.let {
                        TitledContent(
                            title = stringResource(Res.string.pages),
                        ) {
                            BookChip {
                                Text(text = it.toString())
                            }
                        }
                    }
                }

                if(state.book.languages.isNotEmpty()) {
                    TitledContent(
                        modifier = Modifier.padding(vertical = 8.dp),
                        title = stringResource(Res.string.languages)
                    ) {
                        FlowRow(
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            state.book.languages.forEach { lang ->
                                BookChip(
                                    modifier = Modifier.padding(2.dp),
                                    size = ChipSize.SMALL
                                ) {
                                    Text(
                                        text = lang.uppercase(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(
                            top = 24.dp,
                            bottom = 8.dp
                        ),
                    text = stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge
                )

                if(state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        text = if(state.book.description.isNullOrBlank()) {
                            stringResource(Res.string.description_unavailable)
                        } else {
                            state.book.description
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = if(state.book.description.isNullOrBlank()) {
                            Color.Black.copy(alpha = 0.4f)
                        } else Color.Black
                    )
                }
            }
        }
    }
}