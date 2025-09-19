# BubbleTea

BubbleTea is an Android application for ordering your favorite bubble tea drinks.

**[한국어 문서 (Korean Documentation)](README_KO.md)**

## Architecture & Module Structure

This project follows a modern Android architecture with multi-module structure:

- **Architecture Pattern**: MVVM + Jetpack Compose + Unidirectional Data Flow
- **Dependency Injection**: Hilt (Google's recommended DI framework)
- **UI Framework**: 100% Jetpack Compose with Material 3
- **Package Name**: `com.happyhope.bubbletea`
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Build Tools**: Android Gradle Plugin 8.2.0
- **Language**: Kotlin

### 📚 Architecture Documentation

- **[ARCHITECTURE_DECISIONS.md](ARCHITECTURE_DECISIONS.md)** - ⭐ **Summary of key decisions and rationale**
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Complete architecture overview and design decisions
- **[IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md)** - Detailed implementation patterns and code examples
- **[MODULE_SETUP_GUIDE.md](MODULE_SETUP_GUIDE.md)** - Multi-module setup and build configuration

## Features

- **Modern Architecture**: MVVM pattern with Jetpack Compose
- **Modular Design**: Clean separation between features and core functionality
- **Type-Safe Navigation**: Compose Navigation with type safety
- **Material Design 3**: Latest Material Design system with dynamic theming
- **Dependency Injection**: Hilt for compile-time safety and performance
- **Reactive Programming**: StateFlow and Compose state management

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

### Planned Module Structure

```
BubbleTea/
├── app/                    # Main application module & navigation
├── feature/                # Feature-specific modules
│   ├── home/              # Home screen and welcome features
│   ├── menu/              # Menu browsing and item details
│   ├── cart/              # Shopping cart management
│   ├── profile/           # User profile and preferences
│   └── ordering/          # Order placement and tracking
├── core/                  # Core functionality modules
│   ├── ui/                # Shared UI components and theme
│   ├── data/              # Data layer and repositories
│   ├── domain/            # Business logic and use cases  
│   ├── network/           # API clients and network layer
│   └── common/            # Shared utilities and extensions
└── shared/                # Shared resources
    └── resources/         # Common strings, drawables, etc.
```

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