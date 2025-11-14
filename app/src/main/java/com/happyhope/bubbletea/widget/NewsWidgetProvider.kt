package com.happyhope.bubbletea.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.happyhope.bubbletea.MainActivity
import com.happyhope.bubbletea.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * RemoteViews-based app widget provider that displays a scrollable list of news items.
 * 
 * This widget supports:
 * - Scrollable/pageable collection of news items using ListView with RemoteViewsService
 * - Refresh button to update news content
 * - Open App button to launch the main activity
 * - Click on news items to open URLs in Custom Tabs
 */
class NewsWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_REFRESH = "com.happyhope.bubbletea.widget.ACTION_REFRESH"
        const val ACTION_OPEN_APP = "com.happyhope.bubbletea.widget.ACTION_OPEN_APP"
        const val ACTION_OPEN_NEWS_URL = "com.happyhope.bubbletea.widget.ACTION_OPEN_NEWS_URL"
        const val EXTRA_NEWS_URL = "extra_news_url"
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update each widget instance
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_REFRESH -> {
                // Refresh news data
                coroutineScope.launch {
                    val repository = NewsWidgetRepository.getInstance(context)
                    repository.refreshNews()
                    
                    // Notify all widgets to update
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(
                        ComponentName(context, NewsWidgetProvider::class.java)
                    )
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.news_list)
                }
            }
            ACTION_OPEN_APP -> {
                // Open the main app
                val mainIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                context.startActivity(mainIntent)
            }
            ACTION_OPEN_NEWS_URL -> {
                // Open news URL in Custom Tabs
                val url = intent.getStringExtra(EXTRA_NEWS_URL)
                if (!url.isNullOrBlank()) {
                    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    try {
                        context.startActivity(urlIntent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
        // First widget instance added - perform initial setup if needed
    }

    override fun onDisabled(context: Context) {
        // Last widget instance removed - clean up if needed
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_news_collection)

        // Set up the intent for the RemoteViewsService (provides the list items)
        val serviceIntent = Intent(context, NewsWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            // Add a unique data URI to ensure each widget instance gets its own service
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }
        remoteViews.setRemoteAdapter(R.id.news_list, serviceIntent)

        // Set empty view
        remoteViews.setEmptyView(R.id.news_list, R.id.empty_view)

        // Set up refresh button
        val refreshIntent = Intent(context, NewsWidgetProvider::class.java).apply {
            action = ACTION_REFRESH
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.refresh_button, refreshPendingIntent)

        // Set up Open App button
        val openAppIntent = Intent(context, NewsWidgetProvider::class.java).apply {
            action = ACTION_OPEN_APP
        }
        val openAppPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.open_app_button, openAppPendingIntent)

        // Set up click template for list items (combined with fill-in intent from factory)
        val clickIntent = Intent(context, NewsWidgetProvider::class.java).apply {
            action = ACTION_OPEN_NEWS_URL
        }
        val clickPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE // MUTABLE for fill-in
        )
        remoteViews.setPendingIntentTemplate(R.id.news_list, clickPendingIntent)

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }
}
