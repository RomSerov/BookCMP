package org.example.bookapp.feature_book.data.database.db

import androidx.room.RoomDatabase

expect class DbFactory {
    fun create(): RoomDatabase.Builder<FavouriteBookDb>
}