package org.example.bookapp.feature_book.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.bookapp.feature_book.data.database.entities.BookEntity

@Dao
interface FavouriteBookDao {

    @Upsert
    suspend fun upsert(bookEntity: BookEntity)

    @Query("SELECT * FROM BookEntity")
    fun getFavouriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE id = :id")
    suspend fun getFavouriteBook(id: String): BookEntity?

    @Query("DELETE FROM BookEntity WHERE id = :id")
    suspend fun deleteFavouriteBook(id: String)
}