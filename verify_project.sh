#!/bin/bash

# BubbleTea Android Project Structure Verification Script
echo "=== BubbleTea Android Project Structure Verification ==="
echo

# Check if this is a valid Android project
echo "✓ Checking Android project structure..."

# Check root build.gradle exists
if [ -f "build.gradle" ]; then
    echo "✓ Root build.gradle found"
else
    echo "✗ Root build.gradle missing"
    exit 1
fi

# Check settings.gradle exists
if [ -f "settings.gradle" ]; then
    echo "✓ settings.gradle found"
else
    echo "✗ settings.gradle missing"
    exit 1
fi

# Check gradle wrapper exists
if [ -f "gradlew" ] && [ -f "gradlew.bat" ]; then
    echo "✓ Gradle wrapper scripts found"
else
    echo "✗ Gradle wrapper scripts missing"
    exit 1
fi

# Check app module exists
if [ -d "app" ]; then
    echo "✓ App module directory found"
else
    echo "✗ App module directory missing"
    exit 1
fi

# Check app build.gradle exists
if [ -f "app/build.gradle" ]; then
    echo "✓ App build.gradle found"
else
    echo "✗ App build.gradle missing"
    exit 1
fi

# Check AndroidManifest.xml exists
if [ -f "app/src/main/AndroidManifest.xml" ]; then
    echo "✓ AndroidManifest.xml found"
else
    echo "✗ AndroidManifest.xml missing"
    exit 1
fi

# Check MainActivity exists
if [ -f "app/src/main/java/com/happyhope/bubbletea/MainActivity.kt" ]; then
    echo "✓ MainActivity.kt found"
else
    echo "✗ MainActivity.kt missing"
    exit 1
fi

# Check basic resources exist
if [ -f "app/src/main/res/values/strings.xml" ]; then
    echo "✓ strings.xml found"
else
    echo "✗ strings.xml missing"
    exit 1
fi

if [ -f "app/src/main/res/values/themes.xml" ]; then
    echo "✓ themes.xml found"
else
    echo "✗ themes.xml missing"
    exit 1
fi

if [ -f "app/src/main/res/layout/activity_main.xml" ]; then
    echo "✓ activity_main.xml found"
else
    echo "✗ activity_main.xml missing"
    exit 1
fi

# Check launcher icons exist
if [ -f "app/src/main/res/mipmap-mdpi/ic_launcher.png" ]; then
    echo "✓ Launcher icons found"
else
    echo "✗ Launcher icons missing"
    exit 1
fi

echo
echo "=== Project Configuration Verification ==="

# Check package name
if grep -q "com.happyhope.bubbletea" app/build.gradle; then
    echo "✓ Package name configured: com.happyhope.bubbletea"
else
    echo "✗ Package name not configured properly"
    exit 1
fi

# Check minimum SDK
if grep -q "minSdk 24" app/build.gradle; then
    echo "✓ Minimum SDK configured: API 24 (Android 7.0)"
else
    echo "✗ Minimum SDK not configured properly"
    exit 1
fi

# Check target SDK
if grep -q "targetSdk 32" app/build.gradle; then
    echo "✓ Target SDK configured: API 32"
else
    echo "✗ Target SDK not configured properly"
    exit 1
fi

# Check app name
if grep -q "BubbleTea" app/src/main/res/values/strings.xml; then
    echo "✓ App name configured: BubbleTea"
else
    echo "✗ App name not configured properly"
    exit 1
fi

echo
echo "=== Build System Verification ==="

# Check Gradle wrapper version
if [ -f "gradle/wrapper/gradle-wrapper.properties" ]; then
    echo "✓ Gradle wrapper properties found"
    echo "  - Gradle version: $(grep 'distributionUrl' gradle/wrapper/gradle-wrapper.properties | cut -d/ -f6 | cut -d- -f2)"
else
    echo "✗ Gradle wrapper properties missing"
    exit 1
fi

# Check if gradlew is executable
if [ -x "gradlew" ]; then
    echo "✓ gradlew is executable"
else
    echo "✗ gradlew is not executable"
    exit 1
fi

echo
echo "=== GitHub Integration Verification ==="

# Check .gitignore
if [ -f ".gitignore" ]; then
    echo "✓ .gitignore found"
    if grep -q "build/" .gitignore; then
        echo "  - Build directories ignored"
    fi
    if grep -q "*.apk" .gitignore; then
        echo "  - APK files ignored"
    fi
else
    echo "✗ .gitignore missing"
    exit 1
fi

# Check LICENSE
if [ -f "LICENSE" ]; then
    echo "✓ LICENSE found"
else
    echo "✗ LICENSE missing"
    exit 1
fi

# Check README
if [ -f "README.md" ]; then
    echo "✓ README.md found"
else
    echo "✗ README.md missing"
    exit 1
fi

echo
echo "=== All Checks Passed! ==="
echo "✓ Android project structure is complete and properly configured"
echo "✓ Package name: com.happyhope.bubbletea"
echo "✓ Minimum SDK: API 24 (Android 7.0)"
echo "✓ Target SDK: API 32"
echo "✓ App name: BubbleTea"
echo "✓ GitHub integration ready"
echo "✓ Build system configured"
echo
echo "The project is ready to be opened in Android Studio!"
echo "To build the project, run: ./gradlew build"
echo "To install on device, run: ./gradlew installDebug"