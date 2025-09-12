# Module Setup & Build Configuration Guide

## 🏗️ Module Structure Implementation

### 1. Root Project Configuration

#### settings.gradle
```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BubbleTea"

// App module
include ':app'

// Feature modules
include ':feature:home'
include ':feature:menu'
include ':feature:cart'
include ':feature:profile'
include ':feature:ordering'

// Core modules
include ':core:ui'
include ':core:data'
include ':core:domain'
include ':core:network'
include ':core:common'

// Shared modules
include ':shared:resources'
```

#### Root build.gradle
```gradle
buildscript {
    ext.kotlin_version = "1.9.22"
    ext.compose_bom_version = "2024.02.00"
    ext.hilt_version = "2.48"
    ext.lifecycle_version = "2.7.0"
    ext.navigation_version = "2.7.6"
    ext.room_version = "2.6.1"
    ext.retrofit_version = "2.9.0"
    
    repositories {
        google()
        mavenCentral()
    }
    
    dependencies {
        classpath 'com.android.tools.build:gradle:8.2.0'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

### 2. BuildSrc Module for Dependency Management

#### buildSrc/build.gradle.kts
```kotlin
plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}
```

#### buildSrc/src/main/kotlin/Dependencies.kt
```kotlin
object Versions {
    const val compileSdk = 34
    const val minSdk = 24
    const val targetSdk = 34
    const val versionCode = 1
    const val versionName = "1.0"
    
    const val kotlin = "1.9.22"
    const val composeBom = "2024.02.00"
    const val hilt = "2.48"
    const val lifecycle = "2.7.0"
    const val navigation = "2.7.6"
    const val room = "2.6.1"
    const val retrofit = "2.9.0"
    const val okhttp = "4.12.0"
    const val coroutines = "1.7.3"
}

object Libraries {
    // Core Android
    const val coreKtx = "androidx.core:core-ktx:1.12.0"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val activityCompose = "androidx.activity:activity-compose:1.8.2"
    
    // Compose
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial3 = "androidx.compose.material3:material3"
    const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.navigation}"
    
    // ViewModel
    const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    
    // Hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.1.0"
    
    // Network
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    
    // Database
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    
    // Coroutines
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    
    // Image Loading
    const val coil = "io.coil-kt:coil-compose:2.5.0"
    
    // Testing
    const val junit = "junit:junit:4.13.2"
    const val androidTestJunit = "androidx.test.ext:junit:1.1.5"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.5.1"
    const val composeTestJunit = "androidx.compose.ui:ui-test-junit4"
    const val composeTestManifest = "androidx.compose.ui:ui-test-manifest"
    const val composeTooling = "androidx.compose.ui:ui-tooling"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val mockito = "org.mockito:mockito-core:5.8.0"
    const val hiltTesting = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
}
```

#### buildSrc/src/main/kotlin/AndroidModule.kt
```kotlin
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureAndroidLibrary() {
    extensions.configure<LibraryExtension> {
        compileSdk = Versions.compileSdk
        
        defaultConfig {
            minSdk = Versions.minSdk
            targetSdk = Versions.targetSdk
            
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
        }
        
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        
        buildFeatures {
            compose = true
        }
        
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.8"
        }
    }
}
```

### 3. Core Module Configurations

#### core/ui/build.gradle
```gradle
plugins {
    id 'com.android.library'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

apply from: '../../buildSrc/android-library.gradle'

android {
    namespace 'com.happyhope.bubbletea.core.ui'
}

dependencies {
    // Compose BOM
    implementation platform(Libraries.composeBom)
    implementation Libraries.composeUi
    implementation Libraries.composeUiGraphics
    implementation Libraries.composeUiToolingPreview
    implementation Libraries.composeMaterial3
    
    // Core dependencies
    implementation Libraries.coreKtx
    implementation Libraries.lifecycleRuntimeKtx
    
    // Hilt
    implementation Libraries.hiltAndroid
    kapt Libraries.hiltCompiler
    
    // Image loading
    implementation Libraries.coil
    
    // Testing
    testImplementation Libraries.junit
    androidTestImplementation Libraries.androidTestJunit
    androidTestImplementation Libraries.espressoCore
    androidTestImplementation platform(Libraries.composeBom)
    androidTestImplementation Libraries.composeTestJunit
    debugImplementation Libraries.composeTooling
    debugImplementation Libraries.composeTestManifest
}
```

#### core/domain/build.gradle
```gradle
plugins {
    id 'com.android.library'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

apply from: '../../buildSrc/android-library.gradle'

android {
    namespace 'com.happyhope.bubbletea.core.domain'
    buildFeatures {
        compose = false // Domain layer doesn't need Compose
    }
}

dependencies {
    // Core dependencies
    implementation Libraries.coreKtx
    
    // Coroutines
    implementation Libraries.coroutinesCore
    implementation Libraries.coroutinesAndroid
    
    // Hilt
    implementation Libraries.hiltAndroid
    kapt Libraries.hiltCompiler
    
    // Testing
    testImplementation Libraries.junit
    testImplementation Libraries.coroutinesTest
    testImplementation Libraries.mockito
}
```

#### core/data/build.gradle
```gradle
plugins {
    id 'com.android.library'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

apply from: '../../buildSrc/android-library.gradle'

android {
    namespace 'com.happyhope.bubbletea.core.data'
    buildFeatures {
        compose = false
    }
}

dependencies {
    // Module dependencies
    implementation project(':core:domain')
    implementation project(':core:network')
    implementation project(':core:common')
    
    // Core dependencies
    implementation Libraries.coreKtx
    
    // Room
    implementation Libraries.roomRuntime
    implementation Libraries.roomKtx
    kapt Libraries.roomCompiler
    
    // Coroutines
    implementation Libraries.coroutinesCore
    implementation Libraries.coroutinesAndroid
    
    // Hilt
    implementation Libraries.hiltAndroid
    kapt Libraries.hiltCompiler
    
    // Testing
    testImplementation Libraries.junit
    testImplementation Libraries.coroutinesTest
    testImplementation Libraries.mockito
    androidTestImplementation Libraries.androidTestJunit
}
```

#### core/network/build.gradle
```gradle
plugins {
    id 'com.android.library'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

apply from: '../../buildSrc/android-library.gradle'

android {
    namespace 'com.happyhope.bubbletea.core.network'
    buildFeatures {
        compose = false
    }
}

dependencies {
    // Module dependencies
    implementation project(':core:common')
    
    // Core dependencies
    implementation Libraries.coreKtx
    
    // Network
    implementation Libraries.retrofit
    implementation Libraries.retrofitGson
    implementation Libraries.okhttp
    implementation Libraries.okhttpLogging
    
    // Coroutines
    implementation Libraries.coroutinesCore
    implementation Libraries.coroutinesAndroid
    
    // Hilt
    implementation Libraries.hiltAndroid
    kapt Libraries.hiltCompiler
    
    // Testing
    testImplementation Libraries.junit
    testImplementation Libraries.coroutinesTest
    testImplementation Libraries.mockito
}
```

### 4. Feature Module Configurations

#### feature/menu/build.gradle
```gradle
plugins {
    id 'com.android.library'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

apply from: '../../buildSrc/android-library.gradle'

android {
    namespace 'com.happyhope.bubbletea.feature.menu'
}

dependencies {
    // Core module dependencies
    implementation project(':core:ui')
    implementation project(':core:domain')
    implementation project(':core:common')
    implementation project(':shared:resources')
    
    // Compose BOM
    implementation platform(Libraries.composeBom)
    implementation Libraries.composeUi
    implementation Libraries.composeUiGraphics
    implementation Libraries.composeUiToolingPreview
    implementation Libraries.composeMaterial3
    implementation Libraries.composeNavigation
    
    // ViewModel
    implementation Libraries.viewModelCompose
    implementation Libraries.viewModelKtx
    
    // Hilt
    implementation Libraries.hiltAndroid
    implementation Libraries.hiltNavigationCompose
    kapt Libraries.hiltCompiler
    
    // Image loading
    implementation Libraries.coil
    
    // Testing
    testImplementation Libraries.junit
    testImplementation Libraries.coroutinesTest
    testImplementation Libraries.mockito
    androidTestImplementation Libraries.androidTestJunit
    androidTestImplementation Libraries.hiltTesting
    androidTestImplementation platform(Libraries.composeBom)
    androidTestImplementation Libraries.composeTestJunit
    debugImplementation Libraries.composeTooling
    debugImplementation Libraries.composeTestManifest
}
```

### 5. App Module Configuration

#### app/build.gradle
```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

android {
    namespace 'com.happyhope.bubbletea'
    compileSdk Versions.compileSdk

    defaultConfig {
        applicationId "com.happyhope.bubbletea"
        minSdk Versions.minSdk
        targetSdk Versions.targetSdk
        versionCode Versions.versionCode
        versionName Versions.versionName

        testInstrumentationRunner "com.happyhope.bubbletea.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary true
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = '1.8'
    }
    
    buildFeatures {
        compose true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.8'
    }
    
    packaging {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
}

dependencies {
    // Feature modules
    implementation project(':feature:home')
    implementation project(':feature:menu')
    implementation project(':feature:cart')
    implementation project(':feature:profile')
    implementation project(':feature:ordering')
    
    // Core modules
    implementation project(':core:ui')
    implementation project(':core:data')
    implementation project(':core:domain')
    implementation project(':shared:resources')
    
    // Core Android
    implementation Libraries.coreKtx
    implementation Libraries.lifecycleRuntimeKtx
    implementation Libraries.activityCompose
    
    // Compose
    implementation platform(Libraries.composeBom)
    implementation Libraries.composeUi
    implementation Libraries.composeUiGraphics
    implementation Libraries.composeUiToolingPreview
    implementation Libraries.composeMaterial3
    implementation Libraries.composeNavigation
    
    // Hilt
    implementation Libraries.hiltAndroid
    implementation Libraries.hiltNavigationCompose
    kapt Libraries.hiltCompiler
    
    // Testing
    testImplementation Libraries.junit
    androidTestImplementation Libraries.androidTestJunit
    androidTestImplementation Libraries.espressoCore
    androidTestImplementation platform(Libraries.composeBom)
    androidTestImplementation Libraries.composeTestJunit
    androidTestImplementation Libraries.hiltTesting
    kaptAndroidTest Libraries.hiltCompiler
    debugImplementation Libraries.composeTooling
    debugImplementation Libraries.composeTestManifest
}
```

### 6. Build Optimization Configuration

#### gradle.properties
```properties
# Kotlin
kotlin.code.style=official

# Android
android.useAndroidX=true
android.enableJetifier=true

# Build Performance
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true

# Compose
android.defaults.buildfeatures.compose=false

# Kapt
kapt.incremental.apt=true
kapt.use.worker.api=true
```

### 7. ProGuard Configuration

#### app/proguard-rules.pro
```proguard
# Add project specific ProGuard rules here.

# Hilt
-dontwarn com.google.dagger.hilt.**
-keep class com.google.dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.modules.ApplicationContextModule

# Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep data classes
-keep class com.happyhope.bubbletea.core.domain.model.** { *; }
-keep class com.happyhope.bubbletea.core.network.model.** { *; }
```

## 🚀 Migration Strategy

### Phase 1: Core Infrastructure
```bash
# 1. Create core modules
mkdir -p core/{ui,data,domain,network,common}/src/main/java
mkdir -p shared/resources/src/main/res

# 2. Set up build configurations
# Copy build.gradle files to respective modules

# 3. Move existing code to appropriate modules
# Move theme files to core:ui
# Extract domain models
```

### Phase 2: Feature Modules
```bash
# 1. Create feature modules
mkdir -p feature/{home,menu,cart,profile,ordering}/src/main/java

# 2. Extract MainActivity logic to feature modules
# Move screens to feature modules
# Implement navigation
```

### Phase 3: Dependency Injection
```bash
# 1. Add Hilt to all modules
# 2. Create DI modules
# 3. Inject dependencies throughout the app
```

---

*This build configuration guide provides the complete setup for a scalable multi-module Android application using modern development practices.*