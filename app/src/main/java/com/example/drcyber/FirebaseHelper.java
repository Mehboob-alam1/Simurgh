package com.example.drcyber;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    
    public static void initializeFirebase(Context context) {
        try {
            // Initialize Firebase Auth
            FirebaseAuth auth = FirebaseAuth.getInstance();
            
            // Initialize Firebase Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            
            // Initialize Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            
            Log.d(TAG, "Firebase initialized successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage());
            Toast.makeText(context, "Firebase initialization failed. Please check your connection.", Toast.LENGTH_LONG).show();
        }
    }
    
    public static boolean isFirebaseAvailable() {
        try {
            FirebaseAuth.getInstance();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Firebase not available: " + e.getMessage());
            return false;
        }
    }
}
