# App Signing Guide for BubbleTea

This guide will help you set up app signing for releasing your BubbleTea app to Google Play Store.

## 🔑 Why App Signing is Critical

**⚠️ WARNING**: Your signing key is the **ONLY** way to update your app. If you lose it:
- You can NEVER update your app
- You'll have to publish a new app with a different package name
- Users will lose all their data and settings

## 📋 Prerequisites

- Java Development Kit (JDK) installed
- Command line terminal access
- Secure location to store your keystore

## 🔐 Step 1: Generate Release Keystore

### On Linux/macOS:

```bash
keytool -genkey -v \
  -keystore release-keystore.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias bubbletea-release
```

### On Windows:

```cmd
keytool -genkey -v -keystore release-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias bubbletea-release
```

### You'll be asked for:

1. **Keystore password**: Choose a strong password (min 6 characters)
2. **Key password**: Can be same as keystore password or different
3. **Name**: Your name or company name
4. **Organizational Unit**: e.g., "Development"
5. **Organization**: Your company name
6. **City/Locality**: Your city
7. **State/Province**: Your state
8. **Country Code**: Two-letter country code (e.g., KR, US)

### Example Session:

```
Enter keystore password: MyStrongPassword123!
Re-enter new password: MyStrongPassword123!
What is your first and last name?
  [Unknown]:  John Doe
What is the name of your organizational unit?
  [Unknown]:  Development
What is the name of your organization?
  [Unknown]:  BubbleTea Inc
What is the name of your City or Locality?
  [Unknown]:  Seoul
What is the name of your State or Province?
  [Unknown]:  Seoul
What is the two-letter country code for this unit?
  [Unknown]:  KR
Is CN=John Doe, OU=Development, O=BubbleTea Inc, L=Seoul, ST=Seoul, C=KR correct?
  [no]:  yes

Generating 2,048 bit RSA key pair and self-signed certificate (SHA256withRSA) with a validity of 10,000 days
        for: CN=John Doe, OU=Development, O=BubbleTea Inc, L=Seoul, ST=Seoul, C=KR
Enter key password for <bubbletea-release>
        (RETURN if same as keystore password): [Press Enter]
[Storing release-keystore.jks]
```

## 📝 Step 2: Create keystore.properties File

Create a file named `keystore.properties` in your project root directory:

```bash
cd /path/to/BubbleTea
nano keystore.properties
```

Add the following content (replace with your actual values):

```properties
storeFile=release-keystore.jks
storePassword=YOUR_KEYSTORE_PASSWORD
keyAlias=bubbletea-release
keyPassword=YOUR_KEY_PASSWORD
```

**Example:**

```properties
storeFile=release-keystore.jks
storePassword=MyStrongPassword123!
keyAlias=bubbletea-release
keyPassword=MyStrongPassword123!
```

## 💾 Step 3: Backup Your Keystore

**⚠️ CRITICAL**: Backup your keystore immediately!

### What to Backup:

1. `release-keystore.jks` file
2. `keystore.properties` file
3. All passwords used

### Where to Backup:

✅ **Recommended backup locations:**

1. **Cloud Storage** (encrypted)
   - Google Drive (in encrypted folder)
   - Dropbox (in encrypted folder)
   - iCloud (in encrypted folder)

2. **External Storage**
   - USB drive (encrypted)
   - External hard drive (encrypted)
   - CD/DVD (stored securely)

3. **Password Manager**
   - Store all passwords in 1Password, LastPass, or similar
   - Include notes about keystore location

4. **Physical Safe**
   - Print important details
   - Store in fireproof safe

❌ **DON'T:**
- Commit to Git (already in .gitignore)
- Email to yourself unencrypted
- Store only in one location
- Share with anyone

## 🔒 Step 4: Secure Your Keystore

### File Permissions (Linux/macOS):

```bash
chmod 600 keystore.properties
chmod 600 release-keystore.jks
```

### Verify .gitignore:

Ensure these lines are in your `.gitignore`:

```
keystore.properties
*.jks
*.keystore
```

## 🏗️ Step 5: Build Release APK

Your `build.gradle` is already configured. Simply run:

```bash
./gradlew assembleRelease
```

The signed APK will be at:
```
app/build/outputs/apk/release/app-release.apk
```

## 📦 Step 6: Build App Bundle (Recommended)

Google Play prefers App Bundles over APKs:

```bash
./gradlew bundleRelease
```

The signed bundle will be at:
```
app/build/outputs/bundle/release/app-release.aab
```

## ✅ Step 7: Verify Signing

### Verify APK Signature:

```bash
keytool -list -printcert -jarfile app/build/outputs/apk/release/app-release.apk
```

### Verify Bundle Signature:

```bash
jarsigner -verify -verbose -certs app/build/outputs/bundle/release/app-release.aab
```

You should see:
- Certificate fingerprints (SHA256, SHA1, MD5)
- Owner information matching what you entered
- Valid signature

## 🔄 Using Play App Signing (Recommended)

Google Play offers to manage your app signing key:

### Benefits:
- Google stores your app signing key securely
- You can reset your upload key if compromised
- Better security

### How to Set Up:

1. Generate keystore as described above
2. Build and upload your first release
3. In Play Console, enable "Google Play App Signing"
4. Follow Google's instructions to opt in

## 🚨 If You Lose Your Keystore

If you haven't enabled Play App Signing and lose your keystore:

1. **You CANNOT update your existing app**
2. You must publish a new app with:
   - Different package name
   - New listing
   - Starting from zero reviews/downloads
3. Users won't automatically migrate

This is why **backup is critical**!

## 📖 Additional Resources

- [Android Developer Guide - Sign Your App](https://developer.android.com/studio/publish/app-signing)
- [Google Play App Signing](https://support.google.com/googleplay/android-developer/answer/9842756)
- [Play Console Help](https://support.google.com/googleplay/android-developer)

## 🆘 Troubleshooting

### Error: "keystore.properties not found"
- Make sure file is in project root
- Check file name spelling (case-sensitive on Linux/macOS)

### Error: "incorrect password"
- Verify password in keystore.properties
- Check for extra spaces or special characters

### Error: "alias does not exist"
- Verify keyAlias in keystore.properties matches
- List aliases: `keytool -list -keystore release-keystore.jks`

### Build succeeds but APK is unsigned
- Check if keystore.properties file exists
- Verify file permissions
- Check build.gradle configuration

## ✅ Security Checklist

- [ ] Keystore generated with strong password
- [ ] keystore.properties created with correct values
- [ ] Both files backed up to cloud storage
- [ ] Both files backed up to external drive
- [ ] Passwords stored in password manager
- [ ] Files NOT committed to Git
- [ ] File permissions set correctly (Linux/macOS)
- [ ] Backup tested (can rebuild APK from backup)
- [ ] Physical backup created (printed details)
- [ ] Recovery plan documented

---

**Remember**: Your signing key is as important as your source code. Treat it accordingly!
