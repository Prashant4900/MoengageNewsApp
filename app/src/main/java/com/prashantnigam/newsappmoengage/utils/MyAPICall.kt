package com.prashantnigam.newsappmoengage.utils

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A utility class to handle asynchronous API calls.
 *
 * @param callback The callback interface to receive API call results.
 */
class MyAPICall(private val callback: ApiCallback) : AsyncTask<String, Void, String>(){

    /**
     * Performs the API call in the background thread.
     *
     * @param params The URL of the API endpoint.
     * @return The response string from the API call.
     */
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): String {
        var result = ""
        var connection: HttpURLConnection? = null
        try {
            val url = URL(params[0])
            connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"

            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            result = stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        return result
    }

    /**
     * Executes after the API call is completed.
     *
     * @param result The response string from the API call.
     */
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        callback.onResult(result)
    }

    /**
     * Callback interface for receiving API call results.
     */
    interface ApiCallback {
        /**
         * Called when the API call is completed.
         *
         * @param result The response string from the API call.
         */
        fun onResult(result: String)
    }
}