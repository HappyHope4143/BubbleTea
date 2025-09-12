# Code Quality and CI Automation

This document describes the code quality management and CI automation setup for the BubbleTea Android project.

## Code Quality Tools

### 1. Ktlint
- **Purpose**: Kotlin code style enforcement
- **Configuration**: Uses `.editorconfig` for settings
- **Commands**:
  - Check: `./gradlew ktlintCheck`
  - Auto-fix: `./gradlew ktlintFormat`
- **Features**:
  - Enforces Kotlin coding conventions
  - Supports Compose function naming exceptions
  - Generates multiple report formats (plain, checkstyle, SARIF)

### 2. Detekt
- **Purpose**: Static code analysis for Kotlin
- **Configuration**: `detekt.yml`
- **Commands**:
  - Run analysis: `./gradlew detekt`
- **Features**:
  - Checks for code smells and potential bugs
  - Complexity analysis
  - Naming conventions
  - Style violations
  - Generates HTML, XML, TXT, and SARIF reports

### 3. Spotless
- **Purpose**: Code formatting automation
- **Configuration**: Configured in `app/build.gradle`
- **Commands**:
  - Check formatting: `./gradlew spotlessCheck`
  - Apply formatting: `./gradlew spotlessApply`
- **Features**:
  - Kotlin code formatting with ktlint integration
  - Trailing whitespace removal
  - End-of-file newline enforcement
  - Miscellaneous file formatting (Gradle, Markdown, etc.)

## Custom Gradle Tasks

### Code Quality Task
```bash
./gradlew codeQuality
```
Runs all code quality checks: ktlint, detekt, and spotless

### All Tests Task
```bash
./gradlew allTests
```
Runs all unit tests: debug and release variants

## Test Setup

### Unit Tests
- **Location**: `app/src/test/java/`
- **Framework**: JUnit 4
- **Dependencies**: Added Mockito and Robolectric for enhanced testing
- **Example**: `MainActivityTest.kt`

### Instrumentation Tests
- **Location**: `app/src/androidTest/java/`
- **Framework**: AndroidJUnit4 with Compose testing
- **Dependencies**: Espresso and Compose test libraries
- **Example**: `ExampleInstrumentedTest.kt`

## CI/CD Pipeline

### GitHub Actions Workflow
File: `.github/workflows/ci.yml`

**Workflow Steps**:
1. **Environment Setup**
   - Checkout code
   - Setup JDK 17
   - Setup Android SDK
   - Cache Gradle dependencies

2. **Code Quality Checks**
   - Ktlint code style check
   - Detekt static analysis
   - Spotless formatting check

3. **Build and Test**
   - Build debug APK
   - Run unit tests
   - Android lint check
   - Build verification script

4. **Artifact Upload**
   - Build reports
   - Code quality reports
   - APK files

### Running Locally

To run the complete quality and test suite locally:

```bash
# Clean and run all checks
./gradlew clean build codeQuality allTests

# Just quality checks
./gradlew codeQuality

# Just tests
./gradlew allTests
```

## Configuration Files

### `.editorconfig`
Defines coding style rules:
- 4-space indentation for Kotlin
- UTF-8 encoding
- Unix line endings
- Max line length: 120 characters
- Compose function naming exceptions

### `detekt.yml`
Detekt configuration with:
- Disabled formatting rules (handled by ktlint/spotless)
- Magic number exceptions for UI theme files
- Compose-specific naming rules
- Complexity thresholds
- Test exclusions

### `app/build.gradle`
Contains plugin configurations and custom tasks:
- Ktlint plugin settings
- Detekt configuration
- Spotless formatting rules
- Test dependencies
- Custom task definitions

## Benefits

1. **Consistent Code Style**: Automated enforcement of Kotlin coding standards
2. **Early Issue Detection**: Static analysis catches potential bugs before runtime
3. **Automated CI**: All checks run automatically on push/PR
4. **Comprehensive Testing**: Unit and instrumentation test infrastructure
5. **Quality Reports**: Detailed reports for code quality metrics
6. **Developer Productivity**: Auto-fixing capabilities reduce manual work

## Dependencies Added

### Code Quality
```gradle
// Ktlint plugin
id 'org.jlleitschuh.gradle.ktlint' version '12.1.0'

// Detekt
classpath "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.4"
detektPlugins "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.4"

// Spotless
classpath "com.diffplug.spotless:spotless-plugin-gradle:6.23.3"
```

### Testing
```gradle
// Unit testing
testImplementation 'org.mockito:mockito-core:5.8.0'
testImplementation 'androidx.test:core:1.5.0'
testImplementation 'org.robolectric:robolectric:4.11.1'

// Instrumentation testing
androidTestImplementation 'androidx.test:runner:1.5.2'
androidTestImplementation 'androidx.test:rules:1.5.0'
```

## Future Enhancements

- Integration with SonarQube for advanced metrics
- Performance testing setup
- UI testing with Espresso
- Test coverage reporting
- Automated dependency updates
- Integration with code review tools