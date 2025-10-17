package com.happyhope.bubbletea.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.happyhope.bubbletea.util.CustomTabsHelper

/**
 * Action callback for opening news URLs from the widget using Custom Tabs.
 */
class OpenNewsUrlAction : ActionCallback {
    
    companion object {
        val URL_KEY = ActionParameters.Key<String>("news_url")
    }
    
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val url = parameters[URL_KEY] ?: return
        CustomTabsHelper.openUrl(context, url)
    }
}
