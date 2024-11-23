package org.example.bookapp.feature_book.data.database.db

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BookDbConstructor: RoomDatabaseConstructor<FavouriteBookDb> {
    override fun initialize(): FavouriteBookDb
}