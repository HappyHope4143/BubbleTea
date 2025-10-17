package com.happyhope.bubbletea.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

/**
 * Helper object for opening URLs in Custom Tabs with fallback to default browser.
 */
object CustomTabsHelper {
    
    /**
     * Opens the given URL in a Custom Tab if available, otherwise falls back to default browser.
     * 
     * @param context The Android context
     * @param url The URL to open
     * @param toolbarColor Optional toolbar color for the Custom Tab (null for default)
     */
    fun openUrl(context: Context, url: String, toolbarColor: Int? = null) {
        try {
            val uri = Uri.parse(url)
            
            // Build Custom Tabs Intent
            val customTabsIntent = CustomTabsIntent.Builder().apply {
                toolbarColor?.let { 
                    setDefaultColorSchemeParams(
                        androidx.browser.customtabs.CustomTabColorSchemeParams.Builder()
                            .setToolbarColor(it)
                            .build()
                    )
                }
                setShowTitle(true)
                setUrlBarHidingEnabled(false)
            }.build()
            
            // Try to launch with Custom Tabs
            try {
                customTabsIntent.launchUrl(context, uri)
            } catch (e: Exception) {
                // Fallback to default browser if Custom Tabs not available
                val browserIntent = Intent(Intent.ACTION_VIEW, uri)
                if (browserIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(browserIntent)
                }
            }
        } catch (e: Exception) {
            // Silently fail if URL is invalid or no browser available
            e.printStackTrace()
        }
    }
}

/**
 * Composable helper function that provides a callback to open URLs with theme-aware Custom Tabs.
 * 
 * Usage:
 * ```
 * val openUrl = rememberOpenUrlInCustomTabs()
 * Button(onClick = { openUrl("https://example.com") }) {
 *     Text("Open URL")
 * }
 * ```
 */
@Composable
fun rememberOpenUrlInCustomTabs(): (String) -> Unit {
    val context = LocalContext.current
    val toolbarColor = MaterialTheme.colorScheme.primary.toArgb()
    
    return { url: String ->
        CustomTabsHelper.openUrl(context, url, toolbarColor)
    }
}
