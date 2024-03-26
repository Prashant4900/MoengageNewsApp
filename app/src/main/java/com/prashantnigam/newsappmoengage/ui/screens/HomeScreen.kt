package com.prashantnigam.newsappmoengage.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prashantnigam.newsappmoengage.R
import com.prashantnigam.newsappmoengage.model.Article
import com.prashantnigam.newsappmoengage.model.NewsResponse
import com.prashantnigam.newsappmoengage.ui.components.ArticleCard
import com.prashantnigam.newsappmoengage.ui.components.ShimmerEffect

/**
 * Composable function for the Home screen.
 *
 * @param viewModel The ViewModel for the Home screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel
) {
    // Mutable state for holding news response and loading state
    val newsResponseState = remember { mutableStateOf<NewsResponse?>(null) }
    val loadingState = remember { mutableStateOf<HomeScreenViewModel.LoadingState>(
        HomeScreenViewModel.LoadingState.Loading
    ) }

    // Fetch news data when the Home screen is launched
    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    // Observe news articles and loading state changes
    LaunchedEffect(viewModel.newsArticle) {
        viewModel.newsArticle.observeForever {
            newsResponseState.value = it
        }
        viewModel.loadingState.observeForever {
            loadingState.value = it
        }
    }

    // Retrieve articles from the news response
    val articles = newsResponseState.value?.articles

    // Scroll behavior for the top app bar
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // Scaffold for the Home screen layout
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "News App",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    // Icon button for toggling sorting order
                    IconButton(onClick = { viewModel.toggleSortOrder() }) {
                        val iconRes = when (viewModel.currentSortOrder) {
                            HomeScreenViewModel.SortOrder.NEW_TO_OLD -> R.drawable.sort
                            HomeScreenViewModel.SortOrder.OLD_TO_NEW -> R.drawable.sort_old
                        }
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Sort Article as per release"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        // Content area
        Box(modifier = Modifier.padding(it)) {
            // Display shimmer effect while loading
            if (HomeScreenViewModel.LoadingState.Loading == loadingState.value) {
                ShimmerEffect()
            } else if (HomeScreenViewModel.LoadingState.Success == loadingState.value) {
                // Display article list
                ArticleList(articles)
            } else {
                // Display error message if loading fails
                Text(
                    text = (loadingState as HomeScreenViewModel.LoadingState.Failure).error,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
        }
    }
}

/**
 * Composable function to display a list of articles.
 *
 * @param articles The list of articles to display.
 */
@Composable
fun ArticleList(articles: List<Article>?) {
    // LazyColumn for efficient scrolling
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        // Iterate through articles and display ArticleCard
        items(
            count = articles?.size ?: 0
        ) { index ->
            val article = articles?.get(index)
            article?.let {
                if (index == 0) {
                    Column {
                        // Add spacer before first article
                        Spacer(modifier = Modifier.height(20.dp))
                        ArticleCard(article = it)
                    }
                } else {
                    ArticleCard(article = it)
                }
                // Add spacer between articles
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
