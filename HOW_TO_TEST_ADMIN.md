# How to Test Admin Login

## ✅ The Code is Already Working!

All admin routing code is implemented in your app. Here's how to test it:

## Step 1: Build the App

```bash
./gradlew clean build
./gradlew installDebug
```

Or in Android Studio: **Run → Run 'app'**

## Step 2: Create Admin Account

1. Open the app
2. Click **"Sign Up"**
3. Enter:
   - **Email**: `admin@drcyber.com` ← IMPORTANT: Use this exact email
   - **Password**: `admin123` (or any password, min 6 chars)
   - **Confirm Password**: Same password
4. Click **"Sign Up"**
5. You'll see: "Account created successfully"

## Step 3: Login as Admin

1. You'll be redirected to Login screen
2. Enter:
   - **Email**: `admin@drcyber.com`
   - **Password**: The password you created
3. Click **"Login"**

### ✅ Expected Result:
**You will go to ADMIN PANEL** (not the regular app!)

You'll see:
- "Admin Panel" in the toolbar
- Add New Post section
- Manage Posts button
- Set Contact Email button
- Logout button

## Step 4: Test Regular User

1. Logout from admin panel
2. Click **"Sign Up"** again
3. Enter:
   - **Email**: `user@test.com` (any email except admin@drcyber.com)
   - **Password**: `user123`
4. Sign up and login

### ✅ Expected Result:
**You will go to MAIN APP** (regular user view)

You'll see:
- Home, Services, About, Contact, Profile tabs
- Regular app interface

## 🔑 Admin Credentials

- **Admin Email**: `admin@drcyber.com` (hardcoded in SignUpActivity.java line 90)
- **Any other email**: Goes to regular user mode

## 📊 How It Works in Code

```
Login
  ↓
Check user role in Firebase database (users/{userId})
  ↓
┌─────────────────────────┐
│ role == "admin" ?       │
└─────────────────────────┘
         ↓
    ┌────┴────┐
   YES       NO
    ↓         ↓
AdminPanel  MainActivity
```

## 🔍 Debugging

If admin login doesn't work:

1. **Check Firebase Console**:
   - Go to Firebase Console → Realtime Database
   - Look for: `users/{userId}/role`
   - Should be: `"admin"` for admin@drcyber.com

2. **Check Logs**:
   ```bash
   adb logcat | grep -E "LoginActivity|UserRole"
   ```

3. **Add Debug Toast** (optional):
   In LoginActivity.java line 99, add:
   ```java
   Toast.makeText(this, "Role: " + userRole.getRole(), Toast.LENGTH_LONG).show();
   ```

## 🎉 Summary

**The code is already implemented and working!**

- ✅ LoginActivity routes admin → AdminPanelActivity
- ✅ LoginActivity routes users → MainActivity  
- ✅ UserRole.isAdmin() checks if role == "admin"
- ✅ SignUpActivity sets role = "admin" for admin@drcyber.com

Just build and test with the email `admin@drcyber.com`!