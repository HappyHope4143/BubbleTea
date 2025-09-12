# BubbleTea App CI/CD and Testing Documentation

This document describes the automated verification and testing setup for the BubbleTea app.

## Overview

The BubbleTea app has been migrated from the Android View system to Jetpack Compose and includes comprehensive CI/CD automation to ensure code quality and functionality.

## Automated Testing Scripts

### 1. `build_verification.sh`
Basic build verification script that checks:
- Project structure
- Jetpack Compose migration status
- Build success
- Unit test execution
- APK generation
- Configuration verification

**Usage:**
```bash
./build_verification.sh
```

### 2. `emulator_verification.sh`
Comprehensive emulator testing script that:
- Builds the app
- Installs APK on emulator
- Launches the app
- Verifies app is running
- Takes screenshots for UI verification
- Checks for crashes
- Tests app responsiveness

**Usage:**
```bash
./emulator_verification.sh
```

### 3. `comprehensive_test.sh`
Complete test suite that combines all verification checks:
- Environment validation
- Project structure checks
- All Gradle build tasks
- Jetpack Compose verification
- Optional emulator tests (if device/emulator available)

**Usage:**
```bash
./comprehensive_test.sh
```

## GitHub Actions Workflows

### 1. CI Build (`.github/workflows/ci.yml`)
Lightweight continuous integration workflow that runs on every push/PR:
- Linting
- Unit tests
- Debug and release APK builds
- Build verification
- Artifact uploads

**Triggers:** Push and Pull Request to main/master/develop branches

### 2. PR Verification (`.github/workflows/pr-verification.yml`)
Comprehensive PR verification with two jobs:
- **build-and-test**: Standard build verification (Ubuntu)
- **emulator-test**: Full emulator testing (macOS with hardware acceleration)

**Features:**
- Caches Gradle dependencies and AVD snapshots
- Runs on macOS for better emulator performance
- Generates screenshots and test reports
- Uploads all artifacts

**Triggers:** Pull Request to main/master/develop branches

## Testing Locally

### Prerequisites
- Java 17 or higher
- Android SDK with platform-tools
- Android Emulator (optional, for emulator tests)

### Quick Verification
```bash
# Basic build check
./build_verification.sh

# Full test suite
./comprehensive_test.sh
```

### With Emulator
1. Start an Android emulator (API 24+)
2. Run the emulator verification:
```bash
./emulator_verification.sh
```

## CI/CD Features

### Caching
- Gradle dependencies are cached between builds
- AVD snapshots are cached for faster emulator startup

### Artifacts
- Debug and release APKs
- Build reports and test results
- Emulator screenshots
- Detailed logs

### Timeout Protection
- Build job: 30-45 minutes
- Emulator job: 60 minutes
- Individual steps have appropriate timeouts

### Error Handling
- Scripts exit on first error
- Detailed logging to files
- Color-coded console output
- Status reporting for each step

## Supported Configurations

### Android API Levels
- Minimum: API 24 (Android 7.0)
- Target: API 34 (Android 14)
- Emulator testing: API 29 (stable and widely supported)

### Build Types
- Debug builds (with debugging enabled)
- Release builds (optimized, unsigned)

### Testing Environments
- Ubuntu (CI builds)
- macOS (emulator testing with hardware acceleration)
- Local development (all platforms)

## Troubleshooting

### Common Issues

1. **Gradle build fails**
   - Check Java version (requires 17+)
   - Clear Gradle cache: `./gradlew clean`
   - Check `gradle.properties` for AndroidX settings

2. **Emulator tests fail**
   - Ensure emulator is running and unlocked
   - Check ADB connection: `adb devices`
   - Verify API level compatibility (24+)

3. **CI workflow fails**
   - Check workflow logs in GitHub Actions
   - Verify all required files are committed
   - Check for permission issues with shell scripts

### Logs and Debugging
- Build logs: Available in CI artifacts
- Emulator logs: `emulator_test_log.txt`
- Screenshots: Captured during emulator tests
- ADB logs: Available through `adb logcat`

## Project Status

✅ **Build Verification**: Complete and automated  
✅ **Emulator Testing**: Full automation with screenshot capture  
✅ **CI/CD Pipeline**: GitHub Actions workflows active  
✅ **Jetpack Compose**: Migration verified and tested  
✅ **Documentation**: Comprehensive testing documentation  

The BubbleTea app is fully automated for build verification and emulator testing, ensuring code quality and functionality on every change.