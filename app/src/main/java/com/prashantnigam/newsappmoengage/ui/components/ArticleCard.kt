package com.prashantnigam.newsappmoengage.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.prashantnigam.newsappmoengage.R
import com.prashantnigam.newsappmoengage.model.Article
import com.prashantnigam.newsappmoengage.utils.formatDateTime
/**
 * Composable function to display an article card UI.
 *
 * @param article The article to display.
 */
@Composable
fun ArticleCard(
    article: Article,
) {
    // Accessing the context
    val context = LocalContext.current

    // Row to display the article card
    Row {
        // AsyncImage to display the article image
        AsyncImage(
            modifier = Modifier
                .size(86.dp)
                .clip(MaterialTheme.shapes.medium),
            model = ImageRequest.Builder(context).data(article.urlToImage).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // Spacer to add space between image and text
        Spacer(modifier = Modifier.width(8.dp))

        // Column to display article details
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .height(86.dp)
        ) {
            // Text to display article title
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable {
                    // Click listener to open article URL
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse(article.url)
                        if (it.resolveActivity(context.packageManager) != null) {
                            context.startActivity(it)
                        }
                    }
                }
            )

            // Row to display article source and published date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Text to display article source
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.bodySmall,
                )

                // Row to display published date icon and text
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Icon to display published date icon
                    Icon(
                        painter = painterResource(id = R.drawable.schedule),
                        contentDescription = null,
                        modifier = Modifier.width(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    // Text to display published date
                    Text(
                        text = formatDateTime(dateTimeString = article.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
