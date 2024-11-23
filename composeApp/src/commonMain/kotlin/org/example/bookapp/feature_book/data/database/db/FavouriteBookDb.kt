package org.example.bookapp.feature_book.data.database.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.example.bookapp.feature_book.data.database.converter.StringListTypeConverter
import org.example.bookapp.feature_book.data.database.dao.FavouriteBookDao
import org.example.bookapp.feature_book.data.database.entities.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1
)
@TypeConverters(
    StringListTypeConverter::class
)
@ConstructedBy(BookDbConstructor::class)
abstract class FavouriteBookDb: RoomDatabase() {
    abstract val favouriteBookDao: FavouriteBookDao

    companion object {
        const val DB_NAME = "book.db"
    }
}