package org.example.bookapp.feature_book.data.database.db

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DbFactory {

    actual fun create(): RoomDatabase.Builder<FavouriteBookDb> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "BookApp")
            os.contains("mac") -> File(userHome, "Library/Application Support/BookApp")
            else -> File(userHome, ".local/share/BookApp")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, FavouriteBookDb.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}