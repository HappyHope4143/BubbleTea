# 🧋 BubbleTea

BubbleTea is an Android application for ordering your favorite bubble tea drinks with a simple and intuitive user interface.

## ✨ Features

- 🎨 Modern Material Design 3 UI
- 🌙 Light and dark theme support
- 📱 Optimized for Android 7.0+ devices
- 🛒 Easy bubble tea ordering experience
- ⚡ Fast and responsive interface

## 🏗️ Project Structure

This is a modern Android project built with:
- **Package Name**: `com.happyhope.bubbletea`
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Build Tools**: Android Gradle Plugin 8.2.0
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Java 8 or later  
- Android SDK with API level 34
- Git (for contributing)

### Building the Project

```bash
# Clone the repository
git clone https://github.com/HappyHope4143/BubbleTea.git
cd BubbleTea

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
5. Wait for Gradle sync to complete

## 📁 Project Structure

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

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details on how to:
- 🐛 Report bugs
- 💡 Suggest new features  
- 🔧 Submit code changes
- 📝 Improve documentation

## 📋 Code of Conduct

This project adheres to a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with ❤️ using Android Jetpack Compose
- Material Design 3 components
- Thanks to all contributors!