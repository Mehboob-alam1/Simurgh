package com.example.drcyber;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements SliderAdapter.OnSliderButtonClickListener{

    private ViewPager2 viewPager2;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<Blog> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 = view.findViewById(R.id.viewPager);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.slider1, "Secure your tomorrow", "Intelligence that\nprotects"));
        sliderItems.add(new SliderItem(R.drawable.slider2, "Navigate the future", "AI-Driven\nTransformation"));
        sliderItems.add(new SliderItem(R.drawable.slider3, "Beyond Boundaries", "Pioneering the\nDigital Frontier7"));

        viewPager2.setAdapter(new SliderAdapter(sliderItems,this));

        autoSlide();
        
        // Initialize Firebase data loading
        initializeFirebaseData();

        return  view;
    }

    private void autoSlide() {
        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            if (viewPager2.getCurrentItem() < 2) {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            } else {
                viewPager2.setCurrentItem(0);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 4000, 4000); // Auto slide every 4 seconds
    }
    
    private void initializeFirebaseData() {
        try {
            // Initialize Firebase references
            databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child("Home");
            list = new ArrayList<>();
            
            // Set up RecyclerView
            if (recyclerView != null && getContext() != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                
                // Load data from Firebase
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
                                
                                // Update adapter with new data
                                if (adapter == null) {
                                    adapter = new Adapter(getContext(), list);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                                
                                Log.d("HomeFragment", "Loaded " + list.size() + " blog posts from Firebase");
                            } else {
                                Log.d("HomeFragment", "No data found in Firebase for Home category");
                            }
                        } catch (Exception e) {
                            Log.e("HomeFragment", "Error processing Firebase data: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("HomeFragment", "Firebase error: " + error.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            Log.e("HomeFragment", "Error initializing Firebase: " + e.getMessage());
        }
    }

    @Override
    public void onSliderButtonClick() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new AboutFragment());
        transaction.addToBackStack(null); // Optional: Add this transaction to the back stack
        transaction.commit();

        // Notify the Bottom Navigation
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_about);
    }
}