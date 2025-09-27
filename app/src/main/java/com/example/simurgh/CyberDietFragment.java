package com.example.simurgh;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CyberDietFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cyber_diet, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        
        title.setText("Cyber Diet");
        description.setText("Customized Cyber Strength. Tailored security strategies and digital wellness programs designed for your specific environment and needs.");
        
        return view;
    }
}
