package com.prashantnigam.newsappmoengage.repository

import com.prashantnigam.newsappmoengage.utils.MyAPICall

/**
 * Repository class for fetching news data from an API.
 */
class NewsRepository {
    /**
     * Fetches news data from the API.
     *
     * @param callBack The callback to be invoked when the API call is completed.
     */
    fun getNews(callBack: MyAPICall.ApiCallback) {
        // API endpoint URL
        val apiUrl = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"

        // Perform API call using MyAPICall
        MyAPICall(object : MyAPICall.ApiCallback {
            override fun onResult(result: String) {
                // Pass the result to the provided callback
                callBack.onResult(result)
            }
        }).execute(apiUrl)
    }
}
