package com.example.drcyber;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ContactFragment extends Fragment {

    private ImageView imgNewsContact;
    private TextView txtMainHeadingContact, txtContentContact;

    private DatabaseReference databaseReference;
    Blog blog;
    private EditText etFullName, etEmailAddress, etPhoneNumber, etCompanyName, etSubject, etMessage;

    private Button btnSend;
    String value="help@doctorcyber.com";
    
    // Firebase data components
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Blog> list;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);


        imgNewsContact = view.findViewById(R.id.imgNewsContact);
//        txtMainHeadingContact = view.findViewById(R.id.txtMainHeadingContact);
//        txtContentContact = view.findViewById(R.id.txtContentContact);

        etFullName = view.findViewById(R.id.etFullName);
        etEmailAddress = view.findViewById(R.id.etEmailContact);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumberContact);
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etSubject = view.findViewById(R.id.etYourSubject);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSendMessage);
        
        // Initialize RecyclerView for contact posts
        recyclerView = view.findViewById(R.id.contactRecyclerView);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child("Contact");

        // Load Firebase data for contact posts
        loadContactPosts();


        readEmail();

        return view;
    }

    private void readEmail() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("contact");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                     value = snapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sendEmail() {
        String fullName = etFullName.getText().toString();
        String emailAddress = etEmailAddress.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String companyName = etCompanyName.getText().toString();
        String subject = etSubject.getText().toString();
        String message = etMessage.getText().toString();

        String emailBody = "Full Name: " + fullName + "\n"
                + "Email Address: " + emailAddress + "\n"
                + "Phone Number: " + phoneNumber + "\n"
                + "Company Name: " + companyName + "\n"
                + "Message: " + message;

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{value});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            // Handle the case where no email app is available
        }
    }
    
    private void loadContactPosts() {
        try {
            list = new ArrayList<>();
            
            if (recyclerView != null && getContext() != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.exists()) {
                                list.clear();
                                
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    Blog blog = snap.getValue(Blog.class);
                                    if (blog != null) {
                                        list.add(blog);
                                    }
                                }
                                
                                if (adapter == null) {
                                    adapter = new Adapter(getContext(), list);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                                
                                Log.d("ContactFragment", "Loaded " + list.size() + " blog posts from Firebase");
                            } else {
                                Log.d("ContactFragment", "No data found in Firebase for Contact category");
                            }
                        } catch (Exception e) {
                            Log.e("ContactFragment", "Error processing Firebase data: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ContactFragment", "Firebase error: " + error.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            Log.e("ContactFragment", "Error initializing Firebase: " + e.getMessage());
        }
    }

}