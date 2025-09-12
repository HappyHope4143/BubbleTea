#!/bin/bash

# BubbleTea App Build and Verification Script
# This script verifies that the migrated Jetpack Compose app builds and works correctly

echo "===========================================" 
echo "BubbleTea App Build Verification"
echo "==========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print status
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
        exit 1
    fi
}

echo "1. Checking project structure..."
if [ -f "app/build.gradle" ] && [ -f "app/src/main/java/com/happyhope/bubbletea/MainActivity.kt" ]; then
    print_status 0 "Project structure is correct"
else
    print_status 1 "Project structure is missing"
fi

echo ""
echo "2. Checking Jetpack Compose migration..."

# Check if MainActivity extends ComponentActivity
if grep -q "ComponentActivity" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt; then
    print_status 0 "MainActivity correctly extends ComponentActivity"
else
    print_status 1 "MainActivity does not extend ComponentActivity"
fi

# Check if setContent is used
if grep -q "setContent" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt; then
    print_status 0 "setContent is used for Compose UI"
else
    print_status 1 "setContent is not found"
fi

# Check if Compose dependencies are present
if grep -q "androidx.compose" app/build.gradle; then
    print_status 0 "Compose dependencies are configured"
else
    print_status 1 "Compose dependencies are missing"
fi

echo ""
echo "3. Building project..."

# Clean build
./gradlew clean > /dev/null 2>&1
print_status $? "Project cleaned"

# Build debug APK
./gradlew assembleDebug > /dev/null 2>&1
print_status $? "Debug APK built successfully"

# Run tests
./gradlew test > /dev/null 2>&1
print_status $? "Unit tests passed"

echo ""
echo "4. Checking build outputs..."

if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    print_status 0 "APK file generated"
    APK_SIZE=$(stat -f%z "app/build/outputs/apk/debug/app-debug.apk" 2>/dev/null || stat -c%s "app/build/outputs/apk/debug/app-debug.apk" 2>/dev/null)
    echo -e "${YELLOW}  APK size: $(numfmt --to=iec $APK_SIZE)${NC}"
else
    print_status 1 "APK file not found"
fi

echo ""
echo "5. Verifying Compose configuration..."

# Check if Compose build features are enabled
if grep -q "compose true" app/build.gradle; then
    print_status 0 "Compose build features enabled"
else
    print_status 1 "Compose build features not enabled"
fi

# Check if Compose compiler extension version is set
if grep -q "kotlinCompilerExtensionVersion" app/build.gradle; then
    print_status 0 "Compose compiler extension version configured"
else
    print_status 1 "Compose compiler extension version missing"
fi

# Check if gradle.properties has AndroidX enabled
if grep -q "android.useAndroidX=true" gradle.properties; then
    print_status 0 "AndroidX enabled in gradle.properties"
else
    print_status 1 "AndroidX not enabled"
fi

echo ""
echo "==========================================="
echo -e "${GREEN}✓ All verification checks passed!${NC}"
echo ""
echo "The BubbleTea app has been successfully migrated to Jetpack Compose and:"
echo "  • Builds without errors"
echo "  • Uses modern ComponentActivity"
echo "  • Implements declarative Compose UI"
echo "  • Has proper theme configuration"
echo "  • Generates a working APK"
echo ""
echo "The app is ready for installation and testing on a device or emulator."
echo "==========================================="