package com.example.simurgh;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.simurgh.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseUser;






public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        });

        binding.btnSignIn.setOnClickListener(v -> {
            String password = binding.etPassword.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Password is required");
            } else if (!isPasswordValid(password)) {
                binding.etPassword.setError("Password should be at least 8 characters");
            } else if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email is required");
            } else if (!isEmailValid(email)) {
                binding.etEmail.setError("Enter a valid email address");
            } else {
                signInWithEmailPassword(email, password);
            }
        });
    }

    private void signInWithEmailPassword(String email, String password) {
        binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
        authViewModel.signIn(email, password).observe(this, authResult -> {
            if (authResult != null) {
                FirebaseUser user = authResult.getUser();
                if (user != null) {
                    // Check if the email is verified
                    if (user.isEmailVerified()) {
                        updateUI();
                    } else {
                        // Redirect to EmailSentActivity if the email is not verified
                        redirectToEmailSentActivity(user);
                    }
                } else {
                    // Authentication failed, handle the error
                    binding.loadingLayout.getRoot().setVisibility(View.GONE);
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("AuthException", "Not authenticated yet");
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
            }
        });
    }

    private void redirectToEmailSentActivity(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, EmailVerificationActivity.class);
        intent.putExtra("userId", user.getUid());
        intent.putExtra("email", user.getEmail());
        // Add any additional data you want to pass (e.g., username, firstName, etc.)
        startActivity(intent);
        finish(); // Optional: Finish LoginActivity to prevent going back to it
    }

    private boolean isPasswordValid(String password) {
        // Password should be at least 8 characters
        return password.length() >= 8;
    }

    private boolean isEmailValid(String email) {
        // Simple regex for validating email address
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void updateUI() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        authViewModel.getCurrentUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                if (firebaseUser.isEmailVerified()) {
                    updateUI();
                } else {
                    // Redirect to EmailSentActivity if the email is not verified
                    redirectToEmailSentActivity(firebaseUser);
                }
            }
        });
    }
}
