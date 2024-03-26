package com.prashantnigam.newsappmoengage.model

/**
 * Data class representing an article.
 *
 * @property author The author of the article.
 * @property content The content of the article.
 * @property description A short description of the article.
 * @property publishedAt The date and time when the article was published.
 * @property source The source of the article.
 * @property title The title of the article.
 * @property url The URL of the article.
 * @property urlToImage The URL to the image associated with the article.
 */
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)
