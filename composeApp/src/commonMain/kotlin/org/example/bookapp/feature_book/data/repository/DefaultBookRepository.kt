package org.example.bookapp.feature_book.data.repository

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.bookapp.feature_book.data.database.dao.FavouriteBookDao
import org.example.bookapp.feature_book.data.mappers.toBook
import org.example.bookapp.feature_book.data.mappers.toBookEntity
import org.example.bookapp.feature_book.data.network.RemoteBookDataSource
import org.example.bookapp.feature_book.domain.model.Book
import org.example.bookapp.feature_book.domain.repository.BookRepository
import org.example.bookapp.core.domain.DataError
import org.example.bookapp.core.domain.EmptyResult
import org.example.bookapp.core.domain.Result
import org.example.bookapp.core.domain.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favouriteBookDao: FavouriteBookDao
): BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query = query)
            .map {
                it.results.map { searchedBookDto ->
                    searchedBookDto.toBook()
                }
            }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {

        val localResult = favouriteBookDao.getFavouriteBook(id = bookId)

        return if (localResult == null) {
            remoteBookDataSource
                .getBookDetails(bookId = bookId)
                .map { it.description }
        } else {
            Result.Success(data = localResult.description)
        }
    }

    override fun getFavouriteBooks(): Flow<List<Book>> {
        return favouriteBookDao
            .getFavouriteBooks()
            .map { listBookEntity ->
                listBookEntity.map {
                    it.toBook()
                }
            }
    }

    override fun isBookFavourite(id: String): Flow<Boolean> {
        return favouriteBookDao
            .getFavouriteBooks()
            .map { listBookEntity ->
                listBookEntity.any {
                    it.id == id
                }
            }
    }

    override suspend fun markAsFavourite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favouriteBookDao.upsert(bookEntity = book.toBookEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavourite(id: String) {
        favouriteBookDao.deleteFavouriteBook(id = id)
    }

}