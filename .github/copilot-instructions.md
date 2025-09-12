# BubbleTea Android Application

BubbleTea is an Android application for ordering bubble tea drinks. Built using Kotlin with modern Android development practices, Jetpack Compose UI framework, Material Design 3 theming, and targeting Android API 24+ (Android 7.0) devices.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Environment Setup and Prerequisites
- Ensure Java 17+ is installed (OpenJDK 17.0.16+ verified working)
- Android SDK is available at `/usr/local/lib/android/sdk`
- Set environment variables before any Android development work:
  ```bash
  export ANDROID_HOME=/usr/local/lib/android/sdk
  export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools
  ```

### Bootstrap and Build Process
1. **CRITICAL**: gradle.properties file is REQUIRED for AndroidX compatibility. If missing, build will fail with AndroidX dependency errors.

2. **Clean Build**:
   ```bash
   ./gradlew clean
   ```
   - Takes approximately 1-3 seconds after initial setup. NEVER CANCEL. Set timeout to 60+ seconds.

3. **Debug Build**:
   ```bash
   ./gradlew assembleDebug
   ```
   - Takes approximately 6-10 seconds after clean. NEVER CANCEL. Set timeout to 120+ seconds.
   - Produces: `app/build/outputs/apk/debug/app-debug.apk` (~8.3MB)

4. **Release Build**:
   ```bash
   ./gradlew assembleRelease
   ```
   - Takes approximately 45-60 seconds after clean (includes lint vital analysis). NEVER CANCEL. Set timeout to 120+ seconds.
   - Produces: `app/build/outputs/apk/release/app-release-unsigned.apk` (~5.6MB)

5. **Install on Connected Device**:
   ```bash
   ./gradlew installDebug
   ```
   - Requires a connected Android device or running emulator

### Testing and Validation

#### Unit Tests
```bash
./gradlew test
# OR specifically:
./gradlew testDebugUnitTest
```
- `./gradlew test` takes approximately 30-40 seconds (includes compilation of all variants). NEVER CANCEL. Set timeout to 60+ seconds.
- `./gradlew testDebugUnitTest` takes approximately 1-3 seconds (currently no test files exist). NEVER CANCEL. Set timeout to 60+ seconds.
- Note: The project currently has NO unit test files. Tests pass because there are no tests to run.

#### Lint Analysis
```bash
./gradlew lintDebug
```
- Takes approximately 10-20 seconds. NEVER CANCEL. Set timeout to 60+ seconds.
- Generates reports in `app/build/reports/lint-results-debug.html`
- Current lint findings: Minor warning about targetSdk 34 (can be ignored)

#### Full Verification Suite
```bash
./gradlew check
```
- Runs all available checks including lint and tests
- Takes approximately 30-45 seconds. NEVER CANCEL. Set timeout to 60+ seconds.

## Manual Validation Requirements

After making any changes to the codebase, ALWAYS perform these validation steps:

### 1. Build Verification
```bash
export ANDROID_HOME=/usr/local/lib/android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools
./gradlew clean
./gradlew assembleDebug
./gradlew assembleRelease
```

### 2. Test Basic App Functionality
Since there's no emulator available for UI testing, validate that:
- APK files are generated successfully in `app/build/outputs/apk/`
- Debug APK size is approximately 8.3MB
- Release APK size is approximately 5.6MB
- No build errors or failures occur

### 3. Lint and Code Quality
```bash
./gradlew lintDebug
```
- Check generated reports for new issues
- Address any ERROR-level lint findings before committing

### 4. App Content Validation
The app contains a Jetpack Compose-based welcome screen with:
- Welcome message: "Welcome to BubbleTea!"
- Description: "Your favorite bubble tea ordering app"
- Material Design 3 theming with day/night mode support
- Launcher icons for all screen densities

When modifying UI components, verify changes by examining:
- `app/src/main/java/com/happyhope/bubbletea/MainActivity.kt` for Compose UI logic
- `app/src/main/java/com/happyhope/bubbletea/ui/theme/` for theme definitions
- `app/src/main/res/values/strings.xml` for text content
- `app/src/main/java/com/happyhope/bubbletea/MainActivity.kt` for logic changes

## Configuration Details

### Project Structure
```
BubbleTea/
├── .github/workflows/         # CI/CD pipelines
├── app/
│   ├── src/main/
│   │   ├── java/com/happyhope/bubbletea/
│   │   │   ├── MainActivity.kt
│   │   │   └── ui/theme/       # Jetpack Compose theme files
│   │   │       ├── Color.kt
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   │   ├── res/
│   │   │   ├── values/strings.xml
│   │   │   ├── values/themes.xml
│   │   │   └── mipmap-*dpi/ic_launcher.png
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle.properties (CRITICAL - required for AndroidX)
├── build.gradle
├── settings.gradle
├── gradlew / gradlew.bat
├── verify_project.sh
└── build_verification.sh
```

### Key Technical Specifications
- **Language**: Kotlin
- **Package**: `com.happyhope.bubbletea`
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Compile SDK**: API 34
- **Android Gradle Plugin**: 7.4.2
- **Gradle Version**: 8.12
- **Kotlin Version**: 1.9.22
- **Build Tools**: 30.0.3 (auto-installed during build)

### Critical Dependencies
- AndroidX Core KTX 1.12.0
- AppCompat 1.6.1  
- Material Components 1.11.0
- Jetpack Compose BOM 2024.02.00
- Compose UI, Material3, and Activity Compose
- ConstraintLayout 2.1.4
- JUnit 4.13.2 (for future tests)
- Espresso 3.5.1 (for future UI tests)

## Common Commands Reference

### Gradle Tasks
```bash
# View all available tasks
./gradlew tasks

# View verification tasks only  
./gradlew tasks --group=verification

# Clean project
./gradlew clean

# Build both debug and release
./gradlew assemble

# Run lint with fix suggestions
./gradlew lintFix
```

### Project Verification
```bash
# Run the included verification script
chmod +x verify_project.sh
./verify_project.sh
```

### Device Management
```bash
# List connected devices
adb devices

# Install debug APK on connected device
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Known Issues and Workarounds

### 1. AndroidX Configuration
- **Issue**: Build fails with "android.useAndroidX property is not enabled"
- **Fix**: Ensure `gradle.properties` exists with `android.useAndroidX=true`

### 2. Kotlin Version Warnings
- **Issue**: Lint shows Kotlin module compatibility warnings
- **Impact**: Does not affect build success, warnings can be ignored
- **Appears in**: lintAnalyzeDebug and assembleRelease tasks

### 3. Target SDK Warning
- **Issue**: Lint warns about not targeting latest Android version
- **Impact**: Minor compatibility mode implications, app works correctly
- **Current**: Targeting API 34 which is acceptable for production

## Build Timing Expectations

All timing based on Ubuntu Linux environment with Gradle daemon running:

| Task | Typical Time | Timeout Setting |
|------|-------------|-----------------|
| `./gradlew clean` | 1-3 seconds | 60 seconds |
| `./gradlew assembleDebug` | 6-10 seconds | 120 seconds |
| `./gradlew assembleRelease` | 45-60 seconds | 120 seconds |
| `./gradlew test` | 30-40 seconds | 60 seconds |
| `./gradlew testDebugUnitTest` | 1-3 seconds | 60 seconds |
| `./gradlew lintDebug` | 10-20 seconds | 60 seconds |
| `./gradlew check` | 30-45 seconds | 60 seconds |

**CRITICAL**: NEVER CANCEL any build or test command before the timeout. First-time builds or builds after `./gradlew clean` may take longer than subsequent builds due to Gradle caching.

**Note on Initial Setup**: The very first build after cloning or in a fresh environment will take significantly longer (60+ seconds for assembleDebug, 90+ seconds for assembleRelease) due to downloading dependencies and setting up build tools. The timings above apply after initial setup and dependency caching.

## Error Recovery

### Build Failures
1. Check that `gradle.properties` exists and contains AndroidX configuration
2. Run `./gradlew clean` and retry
3. Verify Android SDK environment variables are set
4. Check Gradle daemon with `./gradlew --stop` then retry

### Permission Issues
```bash
chmod +x gradlew
chmod +x verify_project.sh
```

### Dependency Issues
```bash
# Clear Gradle cache if needed
rm -rf ~/.gradle/caches
./gradlew clean build
```

This project is ready for Android development and can be opened in Android Studio for advanced development work.