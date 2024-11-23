package org.example.bookapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.bookapp.common.App
import org.example.bookapp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Compose MultiPlatform BookApp"
        ) {
            App()
        }
    }
}