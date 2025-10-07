package com.example.drcyber;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CyberHygieneFragment extends Fragment {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<Blog> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cyber_hygiene, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        
        title.setText("Cyber Hygiene");
        description.setText("Elevating Cyber Hygiene, Protecting Futures. Comprehensive cybersecurity training and awareness programs to maintain digital health and prevent threats.");
        
        // Initialize Firebase data
        recyclerView = view.findViewById(R.id.cyberHygieneRecyclerView);
        initializeFirebaseData();
        
        return view;
    }
    
    private void initializeFirebaseData() {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child("Services").child("Cyber");
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
                                
                                Log.d("CyberHygieneFragment", "Loaded " + list.size() + " blog posts from Firebase");
                            } else {
                                Log.d("CyberHygieneFragment", "No data found in Firebase for Cyber category");
                            }
                        } catch (Exception e) {
                            Log.e("CyberHygieneFragment", "Error processing Firebase data: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CyberHygieneFragment", "Firebase error: " + error.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            Log.e("CyberHygieneFragment", "Error initializing Firebase: " + e.getMessage());
        }
    }
}
