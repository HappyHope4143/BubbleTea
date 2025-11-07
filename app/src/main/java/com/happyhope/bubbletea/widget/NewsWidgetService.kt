package com.happyhope.bubbletea.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.happyhope.bubbletea.R
import com.happyhope.bubbletea.domain.model.News
import kotlinx.coroutines.runBlocking

/**
 * RemoteViewsService that provides the RemoteViewsFactory for the news widget collection.
 */
class NewsWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return NewsWidgetFactory(applicationContext, intent)
    }
}

/**
 * RemoteViewsFactory that creates views for each news item in the widget's ListView.
 * 
 * This factory runs on a background thread for onDataSetChanged(), so it's safe to perform
 * database operations here.
 */
class NewsWidgetFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private var newsList: List<News> = emptyList()
    private val newsRepository: NewsWidgetRepository = NewsWidgetRepository.getInstance(context)

    override fun onCreate() {
        // Initial setup - load news data
        loadNewsData()
    }

    override fun onDataSetChanged() {
        // Called when the widget is updated (e.g., after refresh)
        // This runs on a background thread, so database operations are safe here
        loadNewsData()
    }

    override fun onDestroy() {
        // Clean up resources if needed
    }

    override fun getCount(): Int = newsList.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position >= newsList.size) {
            // Return empty view if position is out of bounds
            return RemoteViews(context.packageName, R.layout.widget_list_item)
        }

        val news = newsList[position]
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_list_item)

        // Set news title and source
        remoteViews.setTextViewText(R.id.news_title, news.title)
        remoteViews.setTextViewText(R.id.news_source, news.source)

        // Set up click intent with fill-in data
        // This will be combined with the pending intent template set in the widget provider
        val fillInIntent = Intent().apply {
            putExtra(NewsWidgetProvider.EXTRA_NEWS_URL, news.url)
        }
        remoteViews.setOnClickFillInIntent(R.id.news_title, fillInIntent)
        remoteViews.setOnClickFillInIntent(R.id.news_source, fillInIntent)

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        // Return null to use default loading view
        return null
    }

    override fun getViewTypeCount(): Int {
        // We only have one type of view
        return 1
    }

    override fun getItemId(position: Int): Long {
        return if (position < newsList.size) {
            newsList[position].id
        } else {
            position.toLong()
        }
    }

    override fun hasStableIds(): Boolean = true

    /**
     * Load news data from the repository.
     * This method is called on a background thread in onDataSetChanged().
     */
    private fun loadNewsData() {
        // Use runBlocking since we're already on a background thread
        // and RemoteViewsFactory expects synchronous data loading
        newsList = runBlocking {
            try {
                newsRepository.getLocalNews()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
