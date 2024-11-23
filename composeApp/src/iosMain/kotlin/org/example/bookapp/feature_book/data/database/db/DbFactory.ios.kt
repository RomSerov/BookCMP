package org.example.bookapp.feature_book.data.database.db

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DbFactory {

    actual fun create(): RoomDatabase.Builder<FavouriteBookDb> {
        val dbFile = documentDirectory() + "/${FavouriteBookDb.DB_NAME}"
        return Room.databaseBuilder<FavouriteBookDb>(name = dbFile)
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}