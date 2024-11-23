package org.example.bookapp.feature_book.data.database.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DbFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<FavouriteBookDb> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(FavouriteBookDb.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}