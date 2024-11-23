package org.example.bookapp.core.presentation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {

    data class DynamicString(val value: String) : UiText

    class StringResId(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResId -> stringResource(
                resource = id,
                formatArgs = args
            )
        }
    }
}