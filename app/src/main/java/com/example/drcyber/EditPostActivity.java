package com.example.drcyber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class EditPostActivity extends AppCompatActivity {

    private String PN, CN, data;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog dialog;
    Blog blog;
    
    private Toolbar toolbar;
    private EditText etTitle, etDesc;
    private ImageView imageView;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        PN = getIntent().getStringExtra("PN");
        CN = getIntent().getStringExtra("CN");
        data = getIntent().getStringExtra("data");

        initializeViews();
        setupToolbar();

        if (CN.equals("null")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(PN);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(PN).child(CN);
        }

        Gson gson = new Gson();
        blog = gson.fromJson(data, Blog.class);

        if (blog != null) {
            imageUri = Uri.parse(blog.getImageUrl());

            etTitle.setText(blog.getTitle());
            etDesc.setText(blog.getDescription());
            Glide.with(this)
                    .load(blog.getImageUrl())
                    .into(imageView);
        }

        btnUpdate.setOnClickListener(v -> uploadData());
        imageView.setOnClickListener(v -> openFileChooser());
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        imageView = findViewById(R.id.imageView);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Post");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                databaseReference.child(blog.getPushId()).child("imageUrl").setValue(downloadUri.toString());
                                Toast.makeText(this, "Image updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
        }
    }

    private void uploadData() {
        final String title = etTitle.getText().toString().trim();
        final String description = etDesc.getText().toString().trim();

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

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait....");
        dialog.show();
        
        databaseReference.child(blog.getPushId()).child("description").setValue(description);
        databaseReference.child(blog.getPushId()).child("title").setValue(title);
        
        dialog.dismiss();
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}