# BubbleTea App Release Preparation Checklist

This document outlines all tasks required to launch the BubbleTea Android app on Google Play Store.

## 📋 Table of Contents
- [Tasks You Must Do](#tasks-you-must-do)
- [Tasks AI Can Help With](#tasks-ai-can-help-with)
- [Post-Launch Tasks](#post-launch-tasks)

---

## 🔴 Tasks You Must Do

These tasks require business decisions, legal responsibility, or external account creation and must be done by you personally.

### 1. Google Play Console Account Setup (Required)
**Priority: Critical**

- [ ] Create Google Play Console developer account
  - Cost: $25 (one-time registration fee)
  - Time: 24-48 hours (account approval wait)
  - Link: https://play.google.com/console/signup
  - Requirements: Gmail account, payment method, identity verification

- [ ] Complete developer profile
  - Developer/Company name
  - Contact email (publicly visible)
  - Website URL (optional but recommended)
  - Privacy Policy URL (required)

### 2. Legal Documents and Policies (Required)
**Priority: Critical**

- [ ] **Write Privacy Policy**
  - Required: Specify what data the app collects
  - Current app collects:
    - Internet access permission (for News API)
    - AdMob advertising data
    - News data via NewsAPI.org
  - Must be hosted: Accessible at public URL
  - Free hosting options: GitHub Pages, Firebase Hosting, Netlify

- [ ] **Write Terms of Service** (Recommended)
  - Specify app usage conditions
  - Liability limitations
  - Service interruption possibilities

- [ ] **Verify Copyright and Licenses**
  - Confirm open-source library licenses (currently using MIT license)
  - Verify app icon and image copyrights

### 3. App Content and Marketing Materials (Required)
**Priority: High**

- [ ] **App Store Listing Information**
  - [ ] App name (current: "BubbleTea")
  - [ ] Short description (80 characters max)
  - [ ] Full description (4,000 characters max)
    - Key features description
    - Bubble tea ordering feature (currently only news feature implemented)
    - Tech news provision feature
  - [ ] Category selection
    - Recommended: "Food & Drink" or "Lifestyle"

- [ ] **App Screenshots** (Required)
  - Minimum 2, recommended 4-8
  - Size: 16:9 or 9:16 ratio
  - Content: Capture main screens
    - Main screen
    - News screen
    - Widget screen
  - Various screen sizes (phone, tablet)

- [ ] **Feature Graphic** (Required)
  - Size: 1024 x 500 pixels
  - PNG format
  - Banner displayed at top of Play Store

- [ ] **Promotional Video** (Optional, Recommended)
  - YouTube link
  - 30 seconds - 2 minutes length
  - Demo of main app features

### 4. App Signing Key Generation (Required)
**Priority: Critical**

- [ ] **Create Release Keystore**
  - Important: If you lose this key, you cannot update the app!
  - Must backup in secure location (cloud + local)
  - Securely store key information:
    - Keystore file
    - Keystore password
    - Key alias
    - Key password

- [ ] **Set up Play App Signing**
  - Option for Google to manage keys (recommended)
  - Reduces risk of key loss

### 5. App Testing (Required)
**Priority: High**

- [ ] **Test on Real Devices**
  - Test on at least 2-3 different devices
  - Test on various Android versions (API 24-34)
  - Test on different screen sizes (small, medium, large)

- [ ] **Functional Testing**
  - [ ] App launch and close
  - [ ] News loading and display
  - [ ] Browser opens when clicking news
  - [ ] Widget installation and operation
  - [ ] Dark mode switching
  - [ ] Network error handling
  - [ ] AdMob ad display

- [ ] **Performance Testing**
  - App loading time
  - Memory usage
  - Battery consumption
  - Network data usage

### 6. API Keys and Configuration (Required)
**Priority: Critical**

- [ ] **NewsAPI.org API Key**
  - Current: Using free plan
  - Free plan limitations:
    - 100 requests per day
    - Development use only
  - ⚠️ **Paid plan required for production release**
    - Business plan: $449/month
    - Or review alternative news APIs

- [ ] **AdMob Configuration**
  - Already configured: `ca-app-pub-7298112669115589~5298316916`
  - [ ] Verify ad unit IDs
  - [ ] Confirm switch from test ads to real ads
  - [ ] Verify app registration in AdMob account

### 7. App Content Rating (Required)
**Priority: High**

- [ ] Complete content rating questionnaire in Google Play Console
  - Questions about violence, sexual content, drugs, gambling, etc.
  - Current app: Only provides news content, expected rating "Everyone"
  - Time: About 10-15 minutes

### 8. Data Safety Section (Required)
**Priority: Critical**

- [ ] Specify data types collected by app
  - [ ] Location information: Not collected
  - [ ] Personal information: Not collected
  - [ ] App activity: AdMob ad tracking
  - [ ] Device information: Minimal information

- [ ] Data handling practices
  - [ ] Encrypted transmission
  - [ ] Deletion request handling
  - [ ] Data sharing parties (AdMob, NewsAPI)

### 9. Target Audience and Content (Required)
**Priority: High**

- [ ] Select target age
  - Recommended: "18+" (includes news content)
  
- [ ] Family Policy compliance
  - Current app: Contains ads, not for families

### 10. Business-Related Decisions (Optional)
**Priority: Medium**

- [ ] **Monetization Strategy**
  - Current: Only AdMob ads
  - Additional options:
    - Premium version (ad removal)
    - In-app purchases
    - Subscription model

- [ ] **Launch Strategy**
  - [ ] Full launch vs staged rollout
  - [ ] Country selection (Korea only vs global)
  - [ ] Pricing policy (free vs paid)

---

## 🟢 Tasks AI Can Help With

These tasks require technical implementation and can be automated or code-generated by AI.

### 1. Automate App Signing Configuration
**Status: Incomplete** | **Time: 10 minutes**

- [ ] Add signing configuration to `app/build.gradle`
  - Add `signingConfigs` block
  - Apply signing to Release build
  - Configure to use keystore.properties file

**AI will do:**
```kotlin
// Auto-add to build.gradle
signingConfigs {
    release {
        // Read from keystore.properties
    }
}
```

### 2. ProGuard/R8 Optimization Configuration
**Status: Only basic settings** | **Time: 20 minutes**

- [ ] Strengthen ProGuard rules
  - Enable code obfuscation
  - Remove unused code
  - Optimize APK size
  - Add library-specific ProGuard rules

**AI will do:**
```kotlin
buildTypes {
    release {
        minifyEnabled true      // Change from false → true
        shrinkResources true    // Remove unused resources
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### 3. Automate Version Management
**Status: Manual management** | **Time: 15 minutes**

- [ ] Auto-increment version code and version name
- [ ] Link with Git tags
- [ ] Auto-generate build numbers

**Current settings:**
```kotlin
versionCode 1
versionName "1.0"
```

**AI can improve:**
```kotlin
versionCode getVersionCode()  // Based on Git commit count
versionName "1.0.${getVersionCode()}"
```

### 4. Optimize Build Variants
**Status: Only Debug/Release** | **Time: 15 minutes**

- [ ] Add Staging environment
- [ ] Separate API keys per environment
- [ ] Distinguish app names per build type

**AI can add:**
```kotlin
flavorDimensions "environment"
productFlavors {
    dev { }
    staging { }
    production { }
}
```

### 5. Improve Test Coverage
**Status: No tests** | **Time: 1-2 hours**

- [ ] Write unit tests
  - ViewModel tests
  - Repository tests
  - API service tests
  
- [ ] Write UI tests
  - Compose UI tests
  - Screen navigation tests

**AI will do:**
- Generate basic tests for each class
- Set up mock objects
- Write test scenarios

### 6. Strengthen CI/CD Pipeline
**Status: Only basic build** | **Time: 30 minutes**

- [ ] Add GitHub Actions workflows
  - [ ] Automatic release builds
  - [ ] Automatic APK upload
  - [ ] Automated version tagging
  - [ ] Auto-deploy to Google Play (optional)

**AI can add:**
```yaml
- name: Build Release APK
  run: ./gradlew assembleRelease
  
- name: Sign APK
  uses: r0adkll/sign-android-release@v1

- name: Upload to Play Store
  uses: r0adkll/upload-google-play@v1
```

### 7. App Size Optimization
**Status: 15MB (Debug)** | **Time: 30 minutes**

- [ ] Resource optimization
  - Remove unused resources
  - Image compression
  - Use vector drawables

- [ ] APK analysis and optimization
  - Use APK Analyzer
  - Remove unnecessary dependencies
  - Generate App Bundle (recommended)

**AI will do:**
```kotlin
android {
    buildTypes {
        release {
            shrinkResources true
        }
    }
    
    // App Bundle configuration
    bundle {
        language { enableSplit = true }
        density { enableSplit = true }
        abi { enableSplit = true }
    }
}
```

### 8. Security Enhancement
**Status: Basic security** | **Time: 20 minutes**

- [ ] Encrypt API keys
  - Move from BuildConfig to NDK
  - Strengthen obfuscation

- [ ] Network security configuration
  - Force HTTPS usage
  - Certificate pinning (optional)

**AI can add:**
```xml
<!-- network_security_config.xml -->
<network-security-config>
    <base-config cleartextTrafficPermitted="false" />
</network-security-config>
```

### 9. Add Performance Monitoring
**Status: None** | **Time: 30 minutes**

- [ ] Integrate Firebase Crashlytics
  - Collect crash reports
  - Track ANRs

- [ ] Add Firebase Analytics
  - Track user behavior
  - Track screen views

**AI can add:**
```kotlin
// build.gradle
implementation 'com.google.firebase:firebase-crashlytics'
implementation 'com.google.firebase:firebase-analytics'
```

### 10. App Update Mechanism
**Status: None** | **Time: 30 minutes**

- [ ] Integrate In-App Updates API
  - Forced update feature
  - Optional update notifications

**AI can add:**
```kotlin
implementation 'com.google.android.play:app-update:2.1.0'
implementation 'com.google.android.play:app-update-ktx:2.1.0'
```

### 11. Auto-Generate Documentation
**Status: Manual documentation** | **Time: 30 minutes**

- [ ] Generate Privacy Policy template
  - Auto-generate based on app features
  - Multi-language support

- [ ] Generate Terms of Service template
- [ ] Update README
- [ ] Auto-generate Changelog

**AI will do:**
- Generate Markdown documents
- Set up GitHub Pages
- Configure auto-deployment

### 12. Add Localization
**Status: Partial English/Korean** | **Time: 1 hour**

- [ ] Complete multi-language resources
  - [ ] Korean (ko)
  - [ ] English (en)
  - [ ] Other languages (optional)

- [ ] Translate Play Store descriptions
- [ ] Multi-language screenshot versions

**AI will do:**
```xml
<!-- values-ko/strings.xml -->
<resources>
    <string name="app_name">버블티</string>
    <!-- ... -->
</resources>
```

---

## 🟡 Post-Launch Tasks

### Immediate Tasks
- [ ] **Monitor Google Play Console**
  - Check crash reports
  - Respond to user reviews
  - Track ANRs (Application Not Responding)

- [ ] **Monitor Performance Metrics**
  - Monitor Android Vitals indicators
  - Battery consumption
  - App startup time

### Short-term (1-2 weeks)
- [ ] **Incorporate User Feedback**
  - Bug fixes
  - Feature improvements

- [ ] **Prepare First Update**
  - Plan version 1.1
  - Feedback-based improvements

### Mid-term (1-3 months)
- [ ] **Feature Expansion**
  - Actually implement bubble tea ordering
  - Integrate payment system
  - User account system

- [ ] **Marketing**
  - Social media promotion
  - Build website
  - Blog posts

---

## 📊 Priority Summary

### 🔴 Immediate Need (Required Before Launch)
1. Create Google Play Console account - **YOU**
2. Write and host Privacy Policy - **YOU**
3. Generate app signing key - **YOU**
4. Prepare screenshots and marketing materials - **YOU**
5. Add app signing configuration code - **AI**
6. Enable ProGuard optimization - **AI**

### 🟠 High Priority (Recommended Before Launch)
1. Complete real device testing - **YOU**
2. Complete content rating - **YOU**
3. Write Data Safety section - **YOU**
4. Add unit tests - **AI**
5. Optimize app size - **AI**

### 🟡 Medium Priority (Can Do After Launch)
1. Review NewsAPI paid plan - **YOU**
2. Add Firebase Crashlytics - **AI**
3. Add In-App Updates - **AI**

### 🟢 Low Priority (Optional)
1. Create promotional video - **YOU**
2. Expand multi-language support - **AI**
3. Additional build variants - **AI**

---

## 💰 Estimated Costs

### Required Costs
- Google Play Console registration: **$25** (one-time)
- NewsAPI paid plan: **$449/month** or find free alternative
- App signing certificate: **Free** (self-generated)

### Optional Costs
- Privacy Policy hosting: **Free** (use GitHub Pages)
- Firebase free plan: **Free** (up to limits)
- Domain purchase: **$10-15/year** (optional)

### Total Estimated Initial Cost
- **Minimum: $25** (if keeping NewsAPI free plan)
- **Recommended: $474** (including NewsAPI paid plan)

---

## ⏱️ Estimated Time

### Your Work Time
- **Minimum required work**: 4-6 hours
  - Play Console setup: 1 hour
  - Document writing: 2-3 hours
  - Screenshots and materials: 1-2 hours
  - Testing: 1 hour

### AI Work Time
- **Technical implementation**: 2-3 hours
  - Build configuration: 30 minutes
  - Test writing: 1 hour
  - Optimization: 1 hour
  - CI/CD: 30 minutes

### Total Estimated Time
- **Launch preparation complete**: 6-9 hours
- **Review and approval wait**: 1-7 days (Google review)

---

## 🚀 Getting Started

### What You Can Start Right Now

1. **Create Google Play Console Account** (Right now!)
   - Link: https://play.google.com/console/signup
   - Have credit card ready
   - Approval in 24-48 hours

2. **Request from AI** (Show this checklist)
   ```
   "Implement the tasks in the 'Tasks AI Can Help With' section 
   of this checklist in priority order"
   ```

3. **Request Privacy Policy Template**
   ```
   "Write a Privacy Policy draft for the BubbleTea app.
   Include NewsAPI, AdMob, and widget features."
   ```

4. **Request Marketing Materials**
   ```
   "Write app description for Google Play Store in Korean and English.
   Key features: tech news, widgets, offline support"
   ```

---

## 📞 Need Help?

### Google Play Support
- Help Center: https://support.google.com/googleplay/android-developer
- Community Forum: https://support.google.com/googleplay/android-developer/community

### Development Resources
- Android Developer Guide: https://developer.android.com/distribute
- Play Console Documentation: https://support.google.com/googleplay/android-developer

---

## ✅ Next Steps

After reviewing this document:

1. ✅ Review priority task list
2. ✅ Confirm costs and time
3. ✅ Start creating Google Play Console account
4. ✅ Request technical tasks from AI

**Ready? Let's get started! 🚀**
