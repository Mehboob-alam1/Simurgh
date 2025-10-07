package com.example.drcyber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AdminPanelActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog dialog;
    
    private Toolbar toolbar;
    private EditText etTitle, etDesc;
    private ImageView imageView;
    private Spinner firstSpinner, secondSpinner;
    private Button btnAddPost, btnManagePost, btnAddEmail, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        initializeViews();
        setupToolbar();
        setSpinner();
        setupClickListeners();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        imageView = findViewById(R.id.imageView);
        firstSpinner = findViewById(R.id.firstSpinner);
        secondSpinner = findViewById(R.id.secondSpinner);
        btnAddPost = findViewById(R.id.btnAddPost);
        btnManagePost = findViewById(R.id.btnManagePost);
        btnAddEmail = findViewById(R.id.btnAddEmail);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin Panel");
        }
    }

    private void setupClickListeners() {
        btnManagePost.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanelActivity.this, ManagePostActivity.class));
        });

        btnAddEmail.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanelActivity.this, AddEmailActivity.class));
        });

        btnAddPost.setOnClickListener(v -> uploadData());

        imageView.setOnClickListener(v -> openFileChooser());

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminPanelActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void setSpinner() {
        String[] firstSpinnerItems = {"Home", "About", "Services", "Contact"};
        ArrayAdapter<String> firstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, firstSpinnerItems);
        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstSpinner.setAdapter(firstAdapter);

        String[] servicesItems = {"Cyber", "Web3", "XR", "IoT", "Charity"};
        ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, servicesItems);
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Services")) {
                    secondSpinner.setVisibility(View.VISIBLE);
                    secondSpinner.setAdapter(secondAdapter);
                } else {
                    secondSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                secondSpinner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadData() {
        try {
            final String title = etTitle.getText().toString().trim();
            final String description = etDesc.getText().toString().trim();

            // Input validation
            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                etTitle.requestFocus();
                return;
            }

            if (description.isEmpty()) {
                etDesc.setError("Description is required");
                etDesc.requestFocus();
                return;
            }

            if (imageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            if (firstSpinner.getSelectedItem() == null) {
                Toast.makeText(this, "Please select the category to add a post", Toast.LENGTH_SHORT).show();
                return;
            }

            if (firstSpinner.getSelectedItem().toString().equals("Services") && secondSpinner.getSelectedItem() == null) {
                Toast.makeText(this, "Please select sub category for services", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show progress dialog
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading image...");
            dialog.setCancelable(false);
            dialog.show();

            String pushId = UUID.randomUUID().toString();
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            // Upload image to Firebase Storage
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, now get download URL
                        fileReference.getDownloadUrl()
                                .addOnCompleteListener(urlTask -> {
                                    try {
                                        if (urlTask.isSuccessful()) {
                                            Uri downloadUri = urlTask.getResult();
                                            if (downloadUri != null) {
                                                // Update progress message
                                                dialog.setMessage("Saving to database...");
                                                
                                                // Create blog object
                                                Blog blog = new Blog(title, description, downloadUri.toString(), pushId, firstSpinner.getSelectedItem().toString());

                                                // Save to Firebase Realtime Database
                                                DatabaseReference blogRef;
                                                if (secondSpinner.getSelectedItem() != null) {
                                                    // Services with sub-category
                                                    blogRef = databaseReference.child("blogs")
                                                            .child(firstSpinner.getSelectedItem().toString())
                                                            .child(secondSpinner.getSelectedItem().toString())
                                                            .child(pushId);
                                                } else {
                                                    // Regular categories (Home, About, Contact)
                                                    blogRef = databaseReference.child("blogs")
                                                            .child(firstSpinner.getSelectedItem().toString())
                                                            .child(pushId);
                                                }

                                                blogRef.setValue(blog)
                                                        .addOnSuccessListener(aVoid -> {
                                                            dialog.dismiss();
                                                            Toast.makeText(AdminPanelActivity.this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                            clearFields();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            dialog.dismiss();
                                                            String errorMsg = "Failed to save post data";
                                                            if (e.getMessage() != null) {
                                                                errorMsg += ": " + e.getMessage();
                                                            }
                                                            Toast.makeText(AdminPanelActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                                        });
                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(AdminPanelActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            dialog.dismiss();
                                            String errorMsg = "Failed to get image URL";
                                            if (urlTask.getException() != null && urlTask.getException().getMessage() != null) {
                                                errorMsg += ": " + urlTask.getException().getMessage();
                                            }
                                            Toast.makeText(AdminPanelActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(AdminPanelActivity.this, "Error processing image URL", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        String errorMsg = "Failed to upload image";
                        if (e.getMessage() != null) {
                            errorMsg += ": " + e.getMessage();
                        }
                        Toast.makeText(AdminPanelActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    });
                    
        } catch (Exception e) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        try {
            etDesc.setText("");
            etTitle.setText("");
            imageView.setImageURI(null);
            imageUri = null;
            
            // Reset spinners to first item
            firstSpinner.setSelection(0);
            secondSpinner.setVisibility(View.GONE);
            
            // Dismiss dialog safely
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            // Handle any errors in clearing fields
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}

