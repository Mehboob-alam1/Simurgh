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

public class CyberDoctorFragment extends Fragment {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<Blog> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cyber_doctor, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        
        title.setText("Cyber Doctor");
        description.setText("24/7 Support. Constant vigilance for unwavering protection. Our rapid response teams operate around the clock to neutralize threats swiftly.");
        
        // Initialize Firebase data
        recyclerView = view.findViewById(R.id.cyberDoctorRecyclerView);
        initializeFirebaseData();
        
        return view;
    }
    
    private void initializeFirebaseData() {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child("Services").child("Charity");
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
                                
                                Log.d("CyberDoctorFragment", "Loaded " + list.size() + " blog posts from Firebase");
                            } else {
                                Log.d("CyberDoctorFragment", "No data found in Firebase for Charity category");
                            }
                        } catch (Exception e) {
                            Log.e("CyberDoctorFragment", "Error processing Firebase data: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CyberDoctorFragment", "Firebase error: " + error.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            Log.e("CyberDoctorFragment", "Error initializing Firebase: " + e.getMessage());
        }
    }
}
