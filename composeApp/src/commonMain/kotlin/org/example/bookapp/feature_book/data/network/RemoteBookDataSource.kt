package org.example.bookapp.feature_book.data.network

import org.example.bookapp.feature_book.data.network.dto.BookWorkDto
import org.example.bookapp.feature_book.data.network.dto.SearchResponseDto
import org.example.bookapp.core.domain.DataError
import org.example.bookapp.core.domain.Result

interface RemoteBookDataSource {

    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(
        bookId: String
    ): Result<BookWorkDto, DataError.Remote>
}