# BubbleTea

BubbleTea is an Android application for ordering your favorite bubble tea drinks.

**[한국어 문서 (Korean Documentation)](README_KO.md)**

## Project Structure

This is a standard Android project created with:
- **Package Name**: `com.happyhope.bubbletea`
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Build Tools**: Android Gradle Plugin 8.2.0
- **Language**: Kotlin

## Features

- Simple and intuitive user interface
- Material Design 3 theming
- Supports both light and dark themes

## Building the Project

### Prerequisites

- Android Studio Arctic Fox or later
- Java 8 or later
- Android SDK with API level 34

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

### Opening in Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the project directory
4. Click "OK"

## Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/com/happyhope/bubbletea/
│       │   └── MainActivity.kt
│       ├── res/
│       │   ├── layout/
│       │   │   └── activity_main.xml
│       │   ├── values/
│       │   │   ├── colors.xml
│       │   │   ├── strings.xml
│       │   │   └── themes.xml
│       │   ├── values-night/
│       │   │   └── themes.xml
│       │   └── mipmap-*/
│       │       └── ic_launcher.png
│       └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.