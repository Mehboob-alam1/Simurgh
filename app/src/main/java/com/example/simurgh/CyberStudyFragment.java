package com.example.simurgh;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CyberStudyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cyber_study, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        
        title.setText("Cyber Study");
        description.setText("Education & Empowerment. Comprehensive training programs that empower teams to prevent threats and maintain cybersecurity best practices.");
        
        return view;
    }
}
