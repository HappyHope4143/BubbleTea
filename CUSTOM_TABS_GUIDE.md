# Custom Tabs Integration Guide

This guide explains how to use the Custom Tabs functionality to open URLs in the BubbleTea app.

## Overview

The Custom Tabs implementation allows news articles to be opened in an in-app browser with a seamless experience. It automatically falls back to the default browser if Custom Tabs are not available.

## Components

### 1. CustomTabsHelper

Location: `app/src/main/java/com/happyhope/bubbletea/util/CustomTabsHelper.kt`

A utility object that provides methods to open URLs using Chrome Custom Tabs with automatic fallback to the default browser.

**Key Features:**
- Opens URLs in Custom Tabs when available
- Automatically falls back to default browser if Custom Tabs not supported
- Configurable toolbar color
- Graceful error handling for invalid URLs

**Usage in ViewModels or Activities:**

```kotlin
import com.happyhope.bubbletea.util.CustomTabsHelper

// Open a URL with default colors
CustomTabsHelper.openUrl(context, "https://example.com")

// Open a URL with custom toolbar color
val toolbarColor = 0xFF6200EE.toInt()
CustomTabsHelper.openUrl(context, "https://example.com", toolbarColor)
```

### 2. rememberOpenUrlInCustomTabs()

A Composable helper function that provides a theme-aware callback for opening URLs.

**Usage in Compose UI:**

```kotlin
import com.happyhope.bubbletea.util.rememberOpenUrlInCustomTabs

@Composable
fun MyScreen() {
    val openUrl = rememberOpenUrlInCustomTabs()
    
    Button(onClick = { openUrl("https://example.com") }) {
        Text("Open URL")
    }
}
```

**Benefits:**
- Automatically uses the app's primary theme color for the toolbar
- Follows Compose best practices
- Reusable across different screens

### 3. OpenNewsUrlAction

Location: `app/src/main/java/com/happyhope/bubbletea/widget/OpenNewsUrlAction.kt`

A Glance action callback for opening URLs from app widgets.

**Usage in Glance Widgets:**

```kotlin
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback
import com.happyhope.bubbletea.widget.OpenNewsUrlAction

Column(
    modifier = GlanceModifier.clickable(
        actionRunCallback<OpenNewsUrlAction>(
            parameters = actionParametersOf(
                OpenNewsUrlAction.URL_KEY to newsUrl
            )
        )
    )
) {
    // Widget content
}
```

## Implementation Examples

### Example 1: NewsScreen (Already Integrated)

The NewsScreen uses the ViewModel approach where clicks are handled through the NewsViewModel:

```kotlin
// In NewsScreen.kt
NewsItem(
    news = news,
    onClick = { viewModel.handleEvent(NewsEvent.NewsClicked(news)) }
)

// In NewsViewModel.kt
private fun handleNewsClick(news: News) {
    CustomTabsHelper.openUrl(context, news.url)
}
```

### Example 2: NewsWidget (Already Integrated)

The NewsWidget uses the Glance action approach:

```kotlin
// In NewsWidget.kt
Column(
    modifier = GlanceModifier.clickable(
        actionRunCallback<OpenNewsUrlAction>(
            parameters = actionParametersOf(
                OpenNewsUrlAction.URL_KEY to news.url
            )
        )
    )
) {
    // Widget content
}
```

### Example 3: Custom Screen with Compose

For new screens or components, use the Compose helper:

```kotlin
@Composable
fun CustomNewsScreen(newsList: List<News>) {
    val openUrl = rememberOpenUrlInCustomTabs()
    
    LazyColumn {
        items(newsList) { news ->
            Card(
                modifier = Modifier.clickable { 
                    openUrl(news.url) 
                }
            ) {
                Text(text = news.title)
            }
        }
    }
}
```

## Testing

Unit tests are provided to ensure the CustomTabsHelper handles various scenarios:
- Valid URLs
- Invalid URLs
- Empty URLs
- Custom toolbar colors

Location: `app/src/test/java/com/happyhope/bubbletea/util/CustomTabsHelperTest.kt`

## Dependency

The Custom Tabs functionality requires the AndroidX Browser library:

```gradle
implementation "androidx.browser:browser:1.5.0"
```

This dependency has been added to `app/build.gradle`.

## Permissions

The app requires Internet permission to open URLs. This is already configured in the AndroidManifest:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Customization

### Changing Toolbar Color

To use a different color for the Custom Tabs toolbar:

```kotlin
// Use a specific color
val customColor = Color.Blue.toArgb()
CustomTabsHelper.openUrl(context, url, customColor)

// Or in Compose, modify the rememberOpenUrlInCustomTabs function
// to use a different color scheme color
```

### Additional Custom Tabs Options

To add more customization options (e.g., custom menu items, animations), modify the `CustomTabsHelper.openUrl()` method:

```kotlin
val customTabsIntent = CustomTabsIntent.Builder().apply {
    // Existing options...
    
    // Add custom options
    setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
    setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
}.build()
```

## Troubleshooting

### URLs Not Opening

1. **Check Internet Permission**: Ensure `INTERNET` permission is in AndroidManifest.xml
2. **Check URL Format**: URLs must include protocol (http:// or https://)
3. **Test Fallback**: If Custom Tabs fail, the default browser should open
4. **Check Logs**: Errors are printed to logcat for debugging

### Custom Tabs Not Available

The implementation automatically falls back to the default browser if Custom Tabs are not available on the device.

## Best Practices

1. **Always handle errors**: The CustomTabsHelper handles errors internally, but be aware of potential issues
2. **Use appropriate context**: Pass the correct context (Activity or Application) based on your use case
3. **Theme consistency**: Use theme-aware colors for a consistent user experience
4. **Test on various devices**: Different devices may have different browser capabilities

## Future Enhancements

Potential improvements for future versions:

1. Pre-warming Custom Tabs for faster loading
2. Custom menu items for sharing or bookmarking
3. Custom animations for smoother transitions
4. Integration with browser history
5. Offline page handling
