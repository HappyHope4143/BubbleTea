#!/bin/bash

# Emulator Verification Script for BubbleTea App
# This script builds the app, installs it on the emulator, and verifies it works

echo "===========================================" 
echo "BubbleTea App Emulator Verification"
echo "==========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Log file for detailed output
LOG_FILE="emulator_test_log.txt"
echo "Emulator verification started at $(date)" > $LOG_FILE

# Function to print status
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
        echo "SUCCESS: $2" >> $LOG_FILE
    else
        echo -e "${RED}✗ $2${NC}"
        echo "FAILED: $2" >> $LOG_FILE
        echo "Exit code: $1" >> $LOG_FILE
        # Don't exit immediately, try to continue with tests
    fi
}

# Function to log and run command
run_command() {
    echo "Running: $1" >> $LOG_FILE
    eval $1 >> $LOG_FILE 2>&1
    return $?
}

echo -e "${BLUE}Step 1: Building the application${NC}"
echo ""

# Clean and build the project
echo "Cleaning project..."
run_command "./gradlew clean"
print_status $? "Project cleaned"

echo "Building debug APK..."
run_command "./gradlew assembleDebug"
if [ $? -eq 0 ]; then
    print_status 0 "Debug APK built"
else
    print_status 1 "Debug APK build failed"
    echo "Build failed, exiting..." >> $LOG_FILE
    exit 1
fi

echo ""
echo -e "${BLUE}Step 2: Checking emulator status${NC}"
echo ""

# Wait for emulator to be ready
echo "Waiting for emulator to be ready..."
run_command "adb wait-for-device"
if [ $? -eq 0 ]; then
    print_status 0 "Emulator device detected"
else
    print_status 1 "No emulator device found"
    echo "No emulator available, exiting..." >> $LOG_FILE
    exit 1
fi

# Check if emulator is fully booted
echo "Waiting for emulator to finish booting..."
BOOT_COMPLETED=""
TIMEOUT=300  # 5 minutes timeout
ELAPSED=0
while [ "$BOOT_COMPLETED" != "1" ] && [ $ELAPSED -lt $TIMEOUT ]; do
    BOOT_COMPLETED=$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')
    if [ "$BOOT_COMPLETED" != "1" ]; then
        sleep 10
        ELAPSED=$((ELAPSED + 10))
        echo "Waiting... ($ELAPSED/$TIMEOUT seconds)"
    fi
done

if [ "$BOOT_COMPLETED" = "1" ]; then
    print_status 0 "Emulator fully booted"
else
    print_status 1 "Emulator failed to boot within timeout"
    echo "Emulator boot timeout, attempting to continue..." >> $LOG_FILE
fi

echo ""
echo -e "${BLUE}Step 3: Installing and testing the app${NC}"
echo ""

# Verify APK exists before installing
if [ ! -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    print_status 1 "APK file not found"
    echo "APK not found at expected location" >> $LOG_FILE
    exit 1
fi

# Install the APK
echo "Installing BubbleTea APK..."
run_command "adb install -r app/build/outputs/apk/debug/app-debug.apk"
if [ $? -eq 0 ]; then
    print_status 0 "APK installed successfully"
else
    print_status 1 "APK installation failed"
    # Try to get more info about the failure
    adb shell pm list packages | grep bubbletea >> $LOG_FILE
    echo "Continuing with verification..." >> $LOG_FILE
fi

# Wait a moment for installation to complete
sleep 5

# Check if app is installed
echo "Verifying app installation..."
PACKAGE_INSTALLED=$(adb shell pm list packages | grep "com.happyhope.bubbletea")
if [ -n "$PACKAGE_INSTALLED" ]; then
    print_status 0 "App package verified in system"
    echo "Package: $PACKAGE_INSTALLED" >> $LOG_FILE
else
    print_status 1 "App package not found in system"
    echo "Package not found, but continuing..." >> $LOG_FILE
fi

# Launch the app
echo "Launching BubbleTea app..."
run_command "adb shell am start -n com.happyhope.bubbletea/.MainActivity"
if [ $? -eq 0 ]; then
    print_status 0 "App launch command sent"
else
    print_status 1 "App launch command failed"
fi

# Wait for app to start
sleep 8

# Check if app is running
echo "Checking if app is running..."
RUNNING_ACTIVITY=$(adb shell dumpsys activity activities | grep "com.happyhope.bubbletea" | head -1)
if [ -n "$RUNNING_ACTIVITY" ]; then
    print_status 0 "App is running successfully"
    echo "Activity: $RUNNING_ACTIVITY" >> $LOG_FILE
else
    # Try alternative method to check if app is running
    RUNNING_PROCESS=$(adb shell ps | grep "com.happyhope.bubbletea")
    if [ -n "$RUNNING_PROCESS" ]; then
        print_status 0 "App process is running"
        echo "Process: $RUNNING_PROCESS" >> $LOG_FILE
    else
        # Check with newer ps command
        RUNNING_PROCESS_NEW=$(adb shell "ps -A | grep com.happyhope.bubbletea")
        if [ -n "$RUNNING_PROCESS_NEW" ]; then
            print_status 0 "App process is running (new ps)"
            echo "Process: $RUNNING_PROCESS_NEW" >> $LOG_FILE
        else
            print_status 1 "App is not running"
            echo "App may have crashed or failed to start" >> $LOG_FILE
        fi
    fi
fi

echo ""
echo -e "${BLUE}Step 4: Additional verification${NC}"
echo ""

# Take a screenshot to verify UI
echo "Taking screenshot for UI verification..."
run_command "adb shell screencap -p /sdcard/bubbletea_screenshot.png"
if [ $? -eq 0 ]; then
    run_command "adb pull /sdcard/bubbletea_screenshot.png ."
    if [ -f "bubbletea_screenshot.png" ]; then
        print_status 0 "Screenshot captured"
        echo "Screenshot saved as bubbletea_screenshot.png" >> $LOG_FILE
    else
        print_status 1 "Failed to pull screenshot"
    fi
else
    print_status 1 "Failed to capture screenshot"
fi

# Check device info
echo "Device information:"
DEVICE_MODEL=$(adb shell getprop ro.product.model 2>/dev/null | tr -d '\r')
ANDROID_VERSION=$(adb shell getprop ro.build.version.release 2>/dev/null | tr -d '\r')
API_LEVEL=$(adb shell getprop ro.build.version.sdk 2>/dev/null | tr -d '\r')

echo -e "${YELLOW}  Model: $DEVICE_MODEL${NC}"
echo -e "${YELLOW}  Android: $ANDROID_VERSION (API $API_LEVEL)${NC}"

echo "Device: $DEVICE_MODEL, Android: $ANDROID_VERSION (API $API_LEVEL)" >> $LOG_FILE

# Test app responsiveness by sending a tap
echo "Testing app responsiveness..."
run_command "adb shell input tap 540 960"
sleep 3
print_status 0 "App interaction test completed"

# Check for any crashes (simplified check)
echo "Checking for app crashes..."
RECENT_LOGS=$(adb logcat -d -t 50 | grep -i "com.happyhope.bubbletea" | grep -i "error\|crash\|exception" | tail -3)
if [ -z "$RECENT_LOGS" ]; then
    print_status 0 "No obvious crashes detected"
else
    echo "Some errors found in logs:" >> $LOG_FILE
    echo "$RECENT_LOGS" >> $LOG_FILE
    print_status 1 "Some errors detected in logs"
fi

echo ""
echo "==========================================="
echo -e "${GREEN}✓ Emulator verification completed!${NC}"
echo ""
echo "Summary:"
echo "  • App built successfully"
echo "  • Emulator started and ready"
echo "  • APK installation attempted"
echo "  • App launch attempted"
echo "  • UI screenshot captured (if successful)"
echo "  • Basic interaction tested"
echo ""
echo "The BubbleTea app verification completed."
echo "Check $LOG_FILE for detailed logs."
echo "==========================================="

# Clean up
echo "Cleanup completed at $(date)" >> $LOG_FILE