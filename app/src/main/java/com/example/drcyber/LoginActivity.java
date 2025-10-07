package com.example.drcyber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase with error handling
        try {
            FirebaseHelper.initializeFirebase(this);
            mAuth = FirebaseAuth.getInstance();
            usersRef = FirebaseDatabase.getInstance().getReference("users");
        } catch (Exception e) {
            Toast.makeText(this, "Firebase initialization failed. Please restart the app.", Toast.LENGTH_LONG).show();
            Log.e("LoginActivity", "Firebase init error: " + e.getMessage());
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");

        btnLogin.setOnClickListener(v -> loginUser());
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
        
        // Remove guest login functionality
    }

    private void loginUser() {
        try {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Input validation
            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                etEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                etPassword.requestFocus();
                return;
            }

            // Email format validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Please enter a valid email address");
                etEmail.requestFocus();
                return;
            }

            // Simple admin check - no Firebase role checking needed
            if (email.equals("admin@drcyber.com") && password.equals("admin")) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(LoginActivity.this, "Admin login successful! Redirecting to Admin Panel", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, AdminPanelActivity.class));
                finish();
                return;
            }

            // Show progress dialog for regular users
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            // Firebase authentication for regular users
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        try {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            
                            if (task.isSuccessful()) {
                                // Regular user - go to Main App
                                Toast.makeText(LoginActivity.this, "Login successful! Redirecting to Main App", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // Handle login failure
                                String errorMessage = "Login failed";
                                if (task.getException() != null && task.getException().getMessage() != null) {
                                    errorMessage += ": " + task.getException().getMessage();
                                }
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "An error occurred during login", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        try {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(LoginActivity.this, "Login failed: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                    
        } catch (Exception e) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(LoginActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // If user is already logged in, go to Main App
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        } catch (Exception e) {
            // If there's any error checking current user, just stay on login screen
            Toast.makeText(LoginActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
        }
    }
}

