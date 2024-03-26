package com.prashantnigam.newsappmoengage.utils

import androidx.compose.ui.text.AnnotatedString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Formats a date and time string into a human-readable format.
 *
 * @param dateTimeString The date and time string to format.
 * @return AnnotatedString representing the formatted date and time.
 */
fun formatDateTime(dateTimeString: String): AnnotatedString {
    // Get the current time and date
    val calendar = Calendar.getInstance()
    val currentTimeMillis = System.currentTimeMillis()
    calendar.timeInMillis = currentTimeMillis

    // Define date format
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    try {
        // Parse the input date and time string into milliseconds
        val dateTimeMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            .parse(dateTimeString)?.time ?: return AnnotatedString("")

        // Create a calendar instance for the target time
        val targetTime = Calendar.getInstance()
        targetTime.timeInMillis = dateTimeMillis

        // Determine the format string based on whether the date is today or not
        val formatString = if (calendar.get(Calendar.DAY_OF_YEAR) == targetTime.get(Calendar.DAY_OF_YEAR) &&
            calendar.get(Calendar.YEAR) == targetTime.get(Calendar.YEAR)) {
            "HH:mm" // If today, format as time only
        } else {
            "dd MMM yyyy" // Otherwise, format as date and time
        }

        // Format the date and time according to the determined format string
        val formattedDate = if (formatString == "dd MMM yyyy") {
            dateFormat.format(Date(dateTimeMillis))
        } else {
            SimpleDateFormat(formatString, Locale.getDefault()).format(Date(dateTimeMillis))
        }

        // Return the formatted date and time as AnnotatedString
        return AnnotatedString(formattedDate)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // Return an empty AnnotatedString if an exception occurs
    return AnnotatedString("")
}
