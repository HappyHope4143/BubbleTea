package com.happyhope.bubbletea.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import com.happyhope.bubbletea.MainActivity
import com.happyhope.bubbletea.R
import com.happyhope.bubbletea.domain.model.News
import java.text.SimpleDateFormat
import java.util.*

class NewsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                NewsWidgetContent(context)
            }
        }
    }
}

@Composable
private fun NewsWidgetContent(context: Context) {
    val newsRepository = NewsWidgetRepository.getInstance(context)
    val newsList = newsRepository.getLocalNews()
    
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(16.dp)
            .cornerRadius(16.dp)
    ) {
        // Header with title and refresh button
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = "Latest News",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.onSurface
                ),
                modifier = GlanceModifier.defaultWeight()
            )
            
            Image(
                provider = ImageProvider(R.drawable.ic_refresh),
                contentDescription = "Refresh",
                modifier = GlanceModifier
                    .size(24.dp)
                    .clickable(actionRunCallback<RefreshNewsAction>())
            )
        }
        
        Spacer(modifier = GlanceModifier.height(8.dp))
        
        // News items
        if (newsList.isEmpty()) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No news available\nTap refresh to load",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = GlanceTheme.colors.onSurfaceVariant
                    )
                )
            }
        } else {
            // Display up to 3 news items
            newsList.take(3).forEach { news ->
                NewsWidgetItem(news = news)
                Spacer(modifier = GlanceModifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun NewsWidgetItem(news: News) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(GlanceTheme.colors.surfaceVariant)
            .padding(12.dp)
            .cornerRadius(8.dp)
            .clickable(
                actionStartActivity<MainActivity>()
            )
    ) {
        Text(
            text = news.title,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onSurface
            ),
            maxLines = 2
        )
        
        Spacer(modifier = GlanceModifier.height(4.dp))
        
        if (news.summary.isNotBlank()) {
            Text(
                text = news.summary,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = GlanceTheme.colors.onSurfaceVariant
                ),
                maxLines = 2
            )
            
            Spacer(modifier = GlanceModifier.height(4.dp))
        }
        
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.Start,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = news.source,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = GlanceTheme.colors.primary
                )
            )
            
            Spacer(modifier = GlanceModifier.width(8.dp))
            
            Text(
                text = dateFormat.format(Date(news.createdAt)),
                style = TextStyle(
                    fontSize = 11.sp,
                    color = GlanceTheme.colors.onSurfaceVariant
                )
            )
        }
    }
}
