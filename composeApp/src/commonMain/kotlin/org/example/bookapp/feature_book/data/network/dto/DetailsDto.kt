package org.example.bookapp.feature_book.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class DescriptionDto(
    val value: String
)

@Serializable(with = BookWorkDtoSerializer::class)
data class BookWorkDto(
    val description: String? = null
)