package org.example.bookapp.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.example.bookapp.feature_book.data.database.db.DbFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {

        single<HttpClientEngine> {
            OkHttp.create()
        }

        single {
            DbFactory(androidApplication())
        }
    }