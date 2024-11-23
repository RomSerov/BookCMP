package org.example.bookapp.feature_book.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.core.domain.DataError
import org.example.bookapp.core.domain.EmptyResult
import org.example.bookapp.core.domain.Result

interface BookRepository {

    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>

    suspend fun getBookDescription(bookId: String): Result<String?, DataError>

    fun getFavouriteBooks(): Flow<List<Book>>

    fun isBookFavourite(id: String): Flow<Boolean>

    suspend fun markAsFavourite(book: Book): EmptyResult<DataError.Local>

    suspend fun deleteFromFavourite(id: String)
}