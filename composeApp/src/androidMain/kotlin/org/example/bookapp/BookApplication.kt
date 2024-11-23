package org.example.bookapp

import android.app.Application
import org.example.bookapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class BookApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@BookApplication)
        }
    }
}