# NewsAPI Integration Guide

## Overview
This application integrates with [NewsAPI.org](https://newsapi.org/) to provide real-time IT and science news articles.

## Features
- ✅ Real-time news fetching from NewsAPI.org
- ✅ Technology category filtering (IT/Science news)
- ✅ Secure API key management via BuildConfig
- ✅ Offline-first architecture with Room database caching
- ✅ Automatic data cleanup (keeps latest 100 articles)
- ✅ Graceful degradation on network failures

## Setup Instructions

### 1. Get Your API Key
1. Visit [https://newsapi.org/register](https://newsapi.org/register)
2. Sign up for a free account
3. Copy your API key from the dashboard

### 2. Configure API Key
The API key is stored securely in `local.properties` which is git-ignored.

```bash
# Copy the template
cp local.properties.template local.properties

# Edit local.properties and add your API key
newsapi.apiKey=YOUR_ACTUAL_API_KEY_HERE
```

**Important:** Never commit `local.properties` to version control as it contains sensitive data.

### 3. Build and Run
```bash
./gradlew clean assembleDebug
./gradlew installDebug
```

## API Usage

### Current Configuration
- **Endpoint:** `/v2/top-headlines`
- **Category:** `technology` (for IT/Science news)
- **Country:** `us` (United States)
- **Page Size:** 20 articles per request
- **Rate Limit:** Free tier = 100 requests/day, 1000 requests/month

### Data Flow
1. **Initial Load:** App checks local database first
2. **Network Refresh:** Fetches latest news from NewsAPI
3. **Cache Storage:** Stores articles in Room database
4. **Fallback:** Uses cached data if network fails
5. **Cleanup:** Maintains only latest 100 articles

## Security Considerations

### API Key Protection
✅ **DO:**
- Store API key in `local.properties`
- Use BuildConfig to inject at compile time
- Add `local.properties` to `.gitignore`
- Document setup in README

❌ **DON'T:**
- Commit API keys to source control
- Hardcode keys in source files
- Share keys in public repositories

### Rate Limiting
The free NewsAPI tier has limits:
- **100 requests per day**
- **1000 requests per month**

The app handles rate limit errors gracefully by:
- Using cached data when API calls fail
- Implementing exponential backoff (future enhancement)
- Providing user feedback on network issues

## Architecture

### Data Layer Components
```
NewsApiService (Retrofit)
    ↓
NewsRepositoryImpl
    ↓
NewsDao (Room)
    ↓
NewsEntity
```

### Key Files
- `NewsApiService.kt` - Retrofit API interface
- `NewsRepositoryImpl.kt` - Data repository with caching logic
- `NewsDataMapper.kt` - Maps API models to domain entities
- `NetworkModule.kt` - Retrofit/OkHttp configuration
- `build.gradle` - BuildConfig API key injection

## Customization

### Changing News Category
Edit `NewsRepositoryImpl.kt`:
```kotlin
val response = newsApiService.getTopHeadlines(
    category = "technology", // Change to: business, science, health, etc.
    country = "us",
    pageSize = 20,
    apiKey = BuildConfig.NEWS_API_KEY
)
```

### Supported Categories
- `business` - Business news
- `technology` - Tech news (current)
- `science` - Science news
- `health` - Health news
- `sports` - Sports news
- `entertainment` - Entertainment news

### Changing Country
```kotlin
country = "us", // Change to: gb, de, fr, jp, kr, etc.
```

### Adjusting Cache Size
Edit `BubbleTeaDatabase.kt`:
```kotlin
const val NEWS_LIMIT = 100 // Change to desired number
```

## Testing

### Unit Tests
```bash
./gradlew testDebugUnitTest
```

Tests cover:
- Repository caching logic
- Data mapping
- Old news cleanup
- Error handling

### Manual Testing Checklist
- [ ] Fresh app install fetches real news
- [ ] Pull-to-refresh updates articles
- [ ] Airplane mode shows cached articles
- [ ] API key error shows appropriate message
- [ ] Rate limit exceeded falls back to cache

## Compliance & Legal

### NewsAPI Terms of Service
⚠️ **Important:** Read and comply with [NewsAPI Terms](https://newsapi.org/terms)

Key requirements:
- ✅ Attribution to news sources (implemented)
- ✅ Link to original articles (implemented)
- ⚠️ Commercial use requires paid plan
- ⚠️ Cannot republish content without permission

### Attribution
The app displays:
- Article source name
- Link to original article
- Publication date

This complies with NewsAPI attribution requirements.

## Troubleshooting

### "API Key Invalid" Error
- Check `local.properties` has correct API key
- Verify no extra spaces or quotes
- Rebuild project: `./gradlew clean assembleDebug`

### No News Showing
- Check internet connection
- Verify API key is active on NewsAPI dashboard
- Check daily/monthly rate limits
- Review logs for API error messages

### Build Errors
- Ensure `local.properties` exists
- Run `./gradlew clean`
- Sync Gradle files in Android Studio
- Verify Android SDK is installed

## Future Enhancements
- [ ] Add search functionality
- [ ] Support multiple news categories
- [ ] Implement pagination for more articles
- [ ] Add image loading for article thumbnails
- [ ] Implement exponential backoff for rate limiting
- [ ] Add user preferences for categories/sources
- [ ] Support multiple languages

## Resources
- [NewsAPI Documentation](https://newsapi.org/docs)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Android BuildConfig](https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code)
