package com.prashantnigam.newsappmoengage.model

/**
 * Data class representing a response from a news API.
 *
 * @property articles The list of articles in the response. Defaults to an empty list.
 * @property status The status of the response. Defaults to an empty string.
 */
data class NewsResponse(
    val articles: List<Article> = emptyList(),
    val status: String = ""
)
