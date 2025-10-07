# Fix Gson Error

## ✅ Gson is Already in build.gradle.kts!

Line 55: `implementation("com.google.code.gson:gson:2.10.1")`

## 🔧 Solution: Sync Gradle

### Option 1: In Android Studio (Recommended)
1. Click **File → Sync Project with Gradle Files**
2. Or click the **"Sync Now"** link that appears at the top of the editor
3. Wait for sync to complete
4. **Clean and rebuild**: **Build → Clean Project**, then **Build → Rebuild Project**

### Option 2: Command Line
```bash
# Navigate to your project directory
cd /Users/mac/Documents/Simurgh

# Clean build
./gradlew clean

# Sync and build
./gradlew build --refresh-dependencies
```

### Option 3: If Still Not Working

If the error persists, try this:

1. **Invalidate Caches in Android Studio**:
   - **File → Invalidate Caches / Restart**
   - Select **"Invalidate and Restart"**

2. **Or manually delete build folders**:
```bash
cd /Users/mac/Documents/Simurgh
rm -rf .gradle
rm -rf app/build
rm -rf build
./gradlew clean build
```

## ✅ After Syncing

Run your app:
```bash
./gradlew installDebug
```

The Gson error should be gone!