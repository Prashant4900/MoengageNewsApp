package com.prashantnigam.newsappmoengage.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.prashantnigam.newsappmoengage.model.NewsResponse
import com.prashantnigam.newsappmoengage.repository.NewsRepository
import com.prashantnigam.newsappmoengage.utils.MyAPICall
/**
 * ViewModel class for the Home screen.
 */
class HomeScreenViewModel : MyAPICall.ApiCallback {

    // LiveData for holding news articles
    private val _newsArticles = MutableLiveData<NewsResponse>()
    val newsArticle: LiveData<NewsResponse> = _newsArticles

    // LiveData for API loading state
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> = _loadingState

    init {
        // Initialize loading state to Loading and fetch news articles
        _loadingState.value = LoadingState.Loading
        fetchNews()
    }

    // Define enum for sorting order
    enum class SortOrder {
        NEW_TO_OLD,
        OLD_TO_NEW
    }

    // Sealed class to represent loading states
    sealed class LoadingState {
        object Loading : LoadingState()
        object Success : LoadingState()
        data class Failure(val error: String) : LoadingState()
    }

    // Initialize sorting order to NEW_TO_OLD by default
    var currentSortOrder = SortOrder.NEW_TO_OLD

    /**
     * Fetches news articles from the API.
     */
    fun fetchNews() {
        // Update loading state to Loading before making API call
        _loadingState.value = LoadingState.Loading
        NewsRepository().getNews(this)
    }

    /**
     * Callback function invoked when the API call result is received.
     *
     * @param result The result string from the API call.
     */
    override fun onResult(result: String) {
        val response = Gson().fromJson(result, NewsResponse::class.java)
        if (response.status == "ok") {
            // Update news articles LiveData with response
            _newsArticles.postValue(response)
            // Update loading state to Success when API call succeeds
            _loadingState.value = LoadingState.Success
        } else {
            // Update loading state to Failure when API call fails
            _loadingState.value = LoadingState.Failure("Something Went Wrong")
        }
    }

    /**
     * Toggles the sorting order of news articles.
     */
    fun toggleSortOrder() {
        currentSortOrder = when (currentSortOrder) {
            SortOrder.NEW_TO_OLD -> SortOrder.OLD_TO_NEW
            SortOrder.OLD_TO_NEW -> SortOrder.NEW_TO_OLD
        }
        // Sort articles based on the current sorting order
        sortArticles()
    }

    /**
     * Sorts news articles based on the current sorting order.
     */
    private fun sortArticles() {
        val sortedArticles = when (currentSortOrder) {
            SortOrder.NEW_TO_OLD -> _newsArticles.value?.articles?.sortedByDescending { it.publishedAt }
            SortOrder.OLD_TO_NEW -> _newsArticles.value?.articles?.sortedBy { it.publishedAt }
        }
        // Update news articles LiveData with sorted articles
        _newsArticles.postValue(_newsArticles.value?.copy(articles = sortedArticles ?: emptyList()))
    }
}
