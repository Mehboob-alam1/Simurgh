package com.example.drcyber;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView txtUserName, txtEmail, txtUserId;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        txtUserName = view.findViewById(R.id.txtUserName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUserId = view.findViewById(R.id.txtUserId);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Load user data
        loadUserData();

        // Logout button click listener
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Set email and user ID from Firebase Auth
            txtEmail.setText(currentUser.getEmail());
            txtUserId.setText("User ID: " + currentUser.getUid());
            
            // Try to get additional user data from database
            usersRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        try {
                            String email = snapshot.child("email").getValue(String.class);
                            String role = snapshot.child("role").getValue(String.class);
                            
                            if (email != null) {
                                txtEmail.setText(email);
                            }
                            
                            // Set username based on role
                            if (role != null && role.equals("admin")) {
                                txtUserName.setText("Admin User");
                            } else {
                                txtUserName.setText("Regular User");
                            }
                        } catch (Exception e) {
                            Log.e("ProfileFragment", "Error parsing user data: " + e.getMessage());
                            txtUserName.setText("User");
                        }
                    } else {
                        txtUserName.setText("User");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("ProfileFragment", "Error loading user data: " + error.getMessage());
                    txtUserName.setText("User");
                }
            });
        } else {
            // User not logged in, redirect to login
            Toast.makeText(getContext(), "Please login to view profile", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void logoutUser() {
        try {
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error during logout: " + e.getMessage());
            Toast.makeText(getContext(), "Error during logout", Toast.LENGTH_SHORT).show();
        }
    }
}