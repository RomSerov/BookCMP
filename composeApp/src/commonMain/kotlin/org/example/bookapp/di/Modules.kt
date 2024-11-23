package org.example.bookapp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.example.bookapp.core.data.HttpClientFactory
import org.example.bookapp.feature_book.data.database.db.DbFactory
import org.example.bookapp.feature_book.data.database.db.FavouriteBookDb
import org.example.bookapp.feature_book.data.network.KtorRemoteBookDataSource
import org.example.bookapp.feature_book.data.network.RemoteBookDataSource
import org.example.bookapp.feature_book.data.repository.DefaultBookRepository
import org.example.bookapp.feature_book.domain.repository.BookRepository
import org.example.bookapp.feature_book.presentation.book_details.BookDetailViewModel
import org.example.bookapp.feature_book.presentation.book_list.BookListViewModel
import org.example.bookapp.feature_book.presentation.common.SelectedBookViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {
        HttpClientFactory.create(engine = get())
    }

    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()

    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single {
        get<DbFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single {
        get<FavouriteBookDb>().favouriteBookDao
    }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
}