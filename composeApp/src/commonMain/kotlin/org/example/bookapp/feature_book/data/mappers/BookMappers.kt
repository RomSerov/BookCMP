package org.example.bookapp.feature_book.data.mappers

import org.example.bookapp.feature_book.data.database.entities.BookEntity
import org.example.bookapp.feature_book.data.network.dto.SearchedBookDto
import org.example.bookapp.feature_book.domain.model.Book

fun SearchedBookDto.toBook(): Book {
    return Book(
        id = this.id.substringAfterLast("/"),
        title = this.title,
        imageUrl = if (this.coverKey != null) {
            "https://covers.openlibrary.org/b/olid/${this.coverKey}-L.jpg"
        } else {
            "https://covers.openlibrary.org/b/id/${this.coverAlternativeKey}-L.jpg"
        },
        authors = this.authorNames ?: emptyList(),
        description = null,
        languages = this.languages ?: emptyList(),
        firstPublishYear = this.firstPublishYear.toString(),
        averageRating = this.ratingsAverage,
        ratingCount = this.ratingsCount,
        numPages = this.numPagesMedian,
        numEditions = this.numEditions ?: 0
    )
}

fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        languages = this.languages,
        authors = this.authors,
        firstPublishYear = this.firstPublishYear,
        ratingsAverage = this.averageRating,
        ratingsCount = this.ratingCount,
        numPagesMedian = this.numPages,
        numEditions = this.numEditions
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        languages = this.languages,
        authors = this.authors,
        firstPublishYear = this.firstPublishYear,
        averageRating = this.ratingsAverage,
        ratingCount = this.ratingsCount,
        numPages = this.numPagesMedian,
        numEditions = this.numEditions
    )
}