#!/bin/bash

# Comprehensive Test Script for BubbleTea App
# This script can be run locally or in CI to verify the entire project

echo "===========================================" 
echo "BubbleTea App Comprehensive Test Suite"
echo "==========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print status
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
        return 1
    fi
}

# Function to print section header
print_section() {
    echo ""
    echo -e "${BLUE}$1${NC}"
    echo "$(printf '=%.0s' {1..50})"
}

# Track overall success
OVERALL_SUCCESS=0

print_section "1. Environment Check"

# Check if Android SDK is available
if command -v adb &> /dev/null; then
    print_status 0 "Android SDK tools available"
else
    print_status 1 "Android SDK tools not found"
    OVERALL_SUCCESS=1
fi

# Check if Java is available
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    print_status 0 "Java available: $JAVA_VERSION"
else
    print_status 1 "Java not found"
    OVERALL_SUCCESS=1
fi

print_section "2. Project Structure Validation"

# Check essential files
REQUIRED_FILES=(
    "app/build.gradle"
    "app/src/main/java/com/happyhope/bubbletea/MainActivity.kt"
    "app/src/main/AndroidManifest.xml"
    "gradle.properties"
    "gradlew"
)

for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        print_status 0 "$file exists"
    else
        print_status 1 "$file missing"
        OVERALL_SUCCESS=1
    fi
done

print_section "3. Gradle Build Tasks"

# Clean the project
echo "Cleaning project..."
if ./gradlew clean > /dev/null 2>&1; then
    print_status 0 "Project cleaned successfully"
else
    print_status 1 "Project clean failed"
    OVERALL_SUCCESS=1
fi

# Run linting
echo "Running lint checks..."
if ./gradlew lint > /dev/null 2>&1; then
    print_status 0 "Lint checks passed"
else
    print_status 1 "Lint checks failed"
    OVERALL_SUCCESS=1
fi

# Compile the project
echo "Compiling project..."
if ./gradlew compileDebugKotlin > /dev/null 2>&1; then
    print_status 0 "Project compilation successful"
else
    print_status 1 "Project compilation failed"
    OVERALL_SUCCESS=1
fi

# Run unit tests
echo "Running unit tests..."
if ./gradlew test > /dev/null 2>&1; then
    print_status 0 "Unit tests passed"
else
    print_status 1 "Unit tests failed"
    OVERALL_SUCCESS=1
fi

# Build debug APK
echo "Building debug APK..."
if ./gradlew assembleDebug > /dev/null 2>&1; then
    print_status 0 "Debug APK built successfully"
    
    # Check APK file
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        APK_SIZE=$(stat -f%z "app/build/outputs/apk/debug/app-debug.apk" 2>/dev/null || stat -c%s "app/build/outputs/apk/debug/app-debug.apk" 2>/dev/null)
        echo -e "${YELLOW}  Debug APK size: $(numfmt --to=iec $APK_SIZE)${NC}"
        print_status 0 "Debug APK file verified"
    else
        print_status 1 "Debug APK file not found"
        OVERALL_SUCCESS=1
    fi
else
    print_status 1 "Debug APK build failed"
    OVERALL_SUCCESS=1
fi

# Build release APK
echo "Building release APK..."
if ./gradlew assembleRelease > /dev/null 2>&1; then
    print_status 0 "Release APK built successfully"
    
    # Check release APK file
    if [ -f "app/build/outputs/apk/release/app-release-unsigned.apk" ]; then
        RELEASE_APK_SIZE=$(stat -f%z "app/build/outputs/apk/release/app-release-unsigned.apk" 2>/dev/null || stat -c%s "app/build/outputs/apk/release/app-release-unsigned.apk" 2>/dev/null)
        echo -e "${YELLOW}  Release APK size: $(numfmt --to=iec $RELEASE_APK_SIZE)${NC}"
        print_status 0 "Release APK file verified"
    else
        print_status 1 "Release APK file not found"
        OVERALL_SUCCESS=1
    fi
else
    print_status 1 "Release APK build failed"
    OVERALL_SUCCESS=1
fi

print_section "4. Jetpack Compose Verification"

# Check Compose-specific configurations
if grep -q "androidx.compose" app/build.gradle; then
    print_status 0 "Compose dependencies configured"
else
    print_status 1 "Compose dependencies missing"
    OVERALL_SUCCESS=1
fi

if grep -q "compose true" app/build.gradle; then
    print_status 0 "Compose build features enabled"
else
    print_status 1 "Compose build features not enabled"
    OVERALL_SUCCESS=1
fi

if grep -q "ComponentActivity" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt; then
    print_status 0 "MainActivity uses ComponentActivity"
else
    print_status 1 "MainActivity doesn't use ComponentActivity"
    OVERALL_SUCCESS=1
fi

if grep -q "setContent" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt; then
    print_status 0 "MainActivity uses setContent for Compose"
else
    print_status 1 "MainActivity doesn't use setContent"
    OVERALL_SUCCESS=1
fi

print_section "5. Optional Emulator Tests"

# Check if emulator is available and running
if command -v adb &> /dev/null; then
    DEVICE_COUNT=$(adb devices | grep -E "device$|emulator" | wc -l)
    
    if [ $DEVICE_COUNT -gt 0 ]; then
        echo -e "${YELLOW}Found $DEVICE_COUNT connected device(s)/emulator(s)${NC}"
        echo "Running emulator verification script..."
        
        if ./emulator_verification.sh; then
            print_status 0 "Emulator tests passed"
        else
            print_status 1 "Emulator tests failed"
            OVERALL_SUCCESS=1
        fi
    else
        echo -e "${YELLOW}No emulator or device connected - skipping emulator tests${NC}"
        print_status 0 "Emulator tests skipped (no devices)"
    fi
else
    echo -e "${YELLOW}ADB not available - skipping emulator tests${NC}"
    print_status 0 "Emulator tests skipped (no ADB)"
fi

print_section "6. Test Results Summary"

if [ $OVERALL_SUCCESS -eq 0 ]; then
    echo ""
    echo -e "${GREEN}==========================================="
    echo "✓ ALL TESTS PASSED!"
    echo "===========================================${NC}"
    echo ""
    echo "The BubbleTea app is ready for:"
    echo "  • Development and testing"
    echo "  • Deployment to devices"
    echo "  • Distribution via app stores"
    echo ""
else
    echo ""
    echo -e "${RED}==========================================="
    echo "✗ SOME TESTS FAILED"
    echo "===========================================${NC}"
    echo ""
    echo "Please review the failed tests above and fix the issues."
    echo ""
fi

exit $OVERALL_SUCCESS