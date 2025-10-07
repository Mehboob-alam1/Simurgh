package com.example.drcyber;

public class UserRole {
    private String email;
    private String role; // "admin" or "user"
    private String userId;

    public UserRole() {
        // Default constructor required for Firebase
    }

    public UserRole(String email, String role, String userId) {
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
}

