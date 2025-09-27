package com.example.simurgh;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CyberHygieneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cyber_hygiene, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        
        title.setText("Cyber Hygiene");
        description.setText("Elevating Cyber Hygiene, Protecting Futures. Comprehensive cybersecurity training and awareness programs to maintain digital health and prevent threats.");
        
        return view;
    }
}
